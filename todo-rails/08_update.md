---
title: PUT /todos/:id
parent: Rails
nav_order: 8
---

This should be pretty straightforward:

```ruby
# spec/routing/todos_spec.rb

RSpec.describe 'todos' do
  # [...]

  it 'PUT todos/:id is routed to todos#update' do
    expect(put: '/todos/42').to route_to(controller: 'todos', action: 'update', id: '42')
  end
end
```

```ruby
# config/routes.rb

Rails.application.routes.draw do
  # [...]

  put 'todos/:id', to: 'todos#update'
end
```

```ruby
# spec/requests/todos_spec.rb
RSpec.describe 'todos' do
  # [...]

  describe 'PUT /todos/:id' do
    context 'when there is a todo with the specified id' do
      let!(:todo) { create(:todo, title: 'some todo') }

      context 'with valid parameters' do
        let(:valid_params) { { title: 'just do it' } }

        it 'responds with http 200' do
          put "/todos/#{todo.id}", params: valid_params
          expect(response).to have_http_status(:ok)
        end

        it 'updates the todo' do
          expect { put "/todos/#{todo.id}", params: valid_params }.to change { todo.reload.title }.to(valid_params[:title])
        end

        it 'responds with the updated todo' do
          put "/todos/#{todo.id}", params: valid_params
          expect(response.body).to eql(todo.reload.to_json)
        end
      end

      context 'with invalid parameters' do
        let(:invalid_params) { { title: '' } }

        it 'responds with http bad request' do
          put "/todos/#{todo.id}", params: invalid_params
          expect(response).to have_http_status(:bad_request)
        end

        it 'responds with an error' do
          put "/todos/#{todo.id}", params: invalid_params
          response_body = JSON.parse(response.body)
          expect(response_body).to eql('error' => 'bad request')
        end
      end
    end

    context 'when there is no todo with the specified id' do
      it 'responds with http 404' do
        put '/todos/42', params: { title: 'just do it' }
        expect(response).to have_http_status(:not_found)
      end

      it 'responds with an error' do
        put '/todos/42', params: { title: 'just do it' }
        response_body = JSON.parse(response.body)
        expect(response_body).to eql('error' => 'not found')
      end
    end
  end
end
```

```ruby
# app/controllers/todos_controller.rb

class TodosController < ApplicationController
  # [...]

  def update
    todo = Todo.find(params[:id])
    todo.update!(todo_params)
    render json: todo
  rescue ActiveRecord::RecordInvalid
    render json: { error: 'bad request' }, status: :bad_request
  rescue ActiveRecord::RecordNotFound
    render json: { error: 'not found' }, status: :not_found
  end

  # [...]
end
```
