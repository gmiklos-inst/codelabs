---
title: GET /todos
parent: Rails
nav_order: 6
---

This should be pretty straightforward:

```ruby
# spec/routing/todos_spec.rb

RSpec.describe 'todos' do
  # [...]

  it 'GET todos/ is routed to todos#index' do
    expect(get: '/todos').to route_to(controller: 'todos', action: 'index')
  end
end
```

```ruby
# config/routes.rb

Rails.application.routes.draw do
  # [...]

  get 'todos', to: 'todos#index'
end
```

```ruby
# spec/requests/todos_spec.rb

RSpec.describe 'todos' do
  # [...]

  describe 'GET /todos' do
    context 'when there are no todos' do
      it 'responds with http 200' do
        get '/todos'
        expect(response).to have_http_status(:ok)
      end

      it 'responds with an empty array as json' do
        get '/todos'
        response_body = JSON.parse(response.body)
        expect(response_body).to eql([])
      end
    end

    context 'when there are some todos' do
      let!(:todos) { create_list(:todo, 3) }

      it 'responds with http 200' do
        get '/todos'
        expect(response).to have_http_status(:ok)
      end

      it 'responds with the todos in an array' do
        get '/todos'
        response_body = JSON.parse(response.body)
        expect(response_body.count).to eq(todos.count)
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
    todos = Todo.all
    render json: todos
  end
end

```
