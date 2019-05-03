---
title: CORS, filtering, paging
parent: Rails
nav_order: 11
---

# CORS

Add the `rack-cors` gem to the `Gemfile`.

```ruby
# config/application.rb

module TodoApi
  class Application < Rails::Application
    # [...]

    config.middleware.insert_before 0, Rack::Cors do
      allow do
        origins '*'
        resource '*', headers: :any, methods: %i[get post put delete options]
      end
    end
  end
end
```

# Filtering

```ruby
# spec/requests/todos_authorized_spec.rb

RSpec.describe 'todos authorized' do
  # [...]

  describe 'GET /todos' do
    # [...]

    context 'when there are some todos' do
      let!(:completed_todos) { create_list(:todo, 2, :completed) }
      let!(:uncompleted_todos) { create_list(:todo, 4, :uncompleted) }
      let(:all_todos) { completed_todos + uncompleted_todos }

      # [...]

      it 'responds with the todos in an array' do
        get '/todos', headers: headers
        response_body = JSON.parse(response.body)
        expect(response_body.count).to eq(all_todos.count)
      end

      it 'can be filtered for completed todos' do
        get '/todos', params: { completed: true }, headers: headers
        response_body = JSON.parse(response.body)
        aggregate_failures do
          expect(response_body.count).to eq(completed_todos.count)
          expect(response_body.map { |todo| todo['completed'] }.all?).to be_truthy
        end
      end

      it 'can be filtered for uncompleted todos' do
        get '/todos', params: { completed: false }, headers: headers
        response_body = JSON.parse(response.body)
        aggregate_failures do
          expect(response_body.count).to eq(uncompleted_todos.count)
          expect(response_body.map { |todo| todo['completed'] }.any?).to be_falsy
        end
      end
    end
  end
end
```

```ruby
# app/controllers/todos_controller.rb

class TodosController < ApplicationController
  # [...]

  def index
    todos = params.key?(:completed) ? Todo.where(completed: params[:completed]) : Todo.all
    render json: todos
  end

  # [...]
end
```

# X-Total-Count header

```ruby
# spec/requests/todos_spec.rb

describe 'GET /todos' do
    context 'when there are no todos' do
      # [...]

      it 'sends X-Total-Count header' do
        get '/todos', headers: headers
        expect(response.headers['X_TOTAL_COUNT']).to eql(0)
      end
    end

    context 'when there are some todos' do
      # [...]

      it 'sends X-Total-Count header' do
        get '/todos', headers: headers
        expect(response.headers['X_TOTAL_COUNT']).to eql(all_todos.count)
      end

      it 'can be filtered for completed todos' do
        get '/todos', params: { completed: true }, headers: headers
        response_body = JSON.parse(response.body)
        aggregate_failures do
          expect(response_body.count).to eq(completed_todos.count)
          expect(response_body.map { |todo| todo['completed'] }.all?).to be_truthy
          expect(response.headers['X_TOTAL_COUNT']).to eql(completed_todos.count)
        end
      end

      it 'can be filtered for uncompleted todos' do
        get '/todos', params: { completed: false }, headers: headers
        response_body = JSON.parse(response.body)
        aggregate_failures do
          expect(response_body.count).to eq(uncompleted_todos.count)
          expect(response_body.map { |todo| todo['completed'] }.any?).to be_falsy
          expect(response.headers['X_TOTAL_COUNT']).to eql(uncompleted_todos.count)
        end
      end
    end
  end
end
```

```ruby
# app/controllers/todos_controller.rb
class TodosController < ApplicationController
  TOTAL_COUNT_HEADER = 'X_TOTAL_COUNT'

  # [...]

  def index
    # [...]
    response.set_header(TOTAL_COUNT_HEADER, todos.count)
    # [...]
  end

  # [...]
end
```

# Pagination

Add the `kaminari` gem to the `Gemfile`.

```ruby
```

```ruby
# app/controllers/todos_controller.rb

class TodosController < ApplicationController
  DEFAULT_PAGE_SIZE = 20

  # [...]

  def index
    response.set_header(TOTAL_COUNT_HEADER, todos.except(:limit, :offset).count)
    render json: todos.page(page).per(per_page)
  end

  # [...]

  private

  # [...]

  def todos
    @todos ||= params.key?(:completed) ? Todo.where(completed: params[:completed]) : Todo.all
  end

  def page
    params[:page].to_i.abs
  end

  def per_page
    page_size = params[:page_size].to_i.abs
    page_size.zero? ? DEFAULT_PAGE_SIZE : page_size
  end
end
```
