---
title: DELETE /todos/:id
parent: Rails
nav_order: 5
---

This should be pretty straightforward:

```ruby
# spec/routing/todos_spec.rb

RSpec.describe 'todos' do
  # [...]

  it 'DELETE todos/:id is routed to todos#destroy' do
    expect(delete: '/todos/42').to route_to(controller: 'todos', action: 'destroy', id: '42')
  end
end
```

```ruby
# config/routes.rb

Rails.application.routes.draw do
  # [...]

  delete 'todos/:id', to: 'todos#destroy'
end
```

```ruby
# spec/requests/todos_spec.rb

RSpec.describe 'todos' do
  # [...]

  describe 'DELETE /todos/:id' do
    context 'when there is a todo with the specified id' do
      let!(:todo) { create(:todo) }

      it 'responds with http 204' do
        delete "/todos/#{todo.id}"
        expect(response).to have_http_status(:no_content)
      end

      it 'deletes the todo' do
        expect { delete "/todos/#{todo.id}" }.to change { Todo.exists?(todo.id) }.from(true).to(false)
      end
    end

    context 'when there is no todo with the specified id' do
      it 'responds with http 404' do
        delete '/todos/42'
        expect(response).to have_http_status(:not_found)
      end

      it 'responds with an error' do
        delete '/todos/42'
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

  def destroy
    todo = Todo.find(params[:id])
    todo.destroy!
    head :no_content
  rescue ActiveRecord::RecordNotFound
    render json: { error: 'not found' }, status: :not_found
  end
end
```
