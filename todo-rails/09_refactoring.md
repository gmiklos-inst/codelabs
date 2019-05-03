---
title: refactoring
parent: Rails
nav_order: 9
---

Let's refactor a bit!

# Error handling

```ruby
# app/controllers/todos_controller.rb

class TodosController < ApplicationController
  rescue_from ActiveRecord::RecordNotFound, with: :render_not_found
  rescue_from ActiveRecord::RecordInvalid, with: :render_bad_request

  def index
    todos = Todo.all
    render json: todos
  end

  def show
    todo = Todo.find(params[:id])
    render json: todo
  end

  def create
    todo = Todo.create!(todo_params)
    render json: todo, status: :created
  end

  def update
    todo = Todo.find(params[:id])
    todo.update!(todo_params)
    render json: todo
  end

  def destroy
    todo = Todo.find(params[:id])
    todo.destroy!
    head :no_content
  end

  private

  def todo_params
    params.permit(:title, :completed)
  end

  def render_not_found
    render json: { error: 'not found' }, status: :not_found
  end

  def render_bad_request
    render json: { error: 'bad request' }, status: :bad_request
  end
end
```

[a nice blog post on serving fancy error messages](https://blog.rebased.pl/2016/11/07/api-error-handling.html)

# Fetching the current todo

```ruby
# app/controllers/todos_controller.rb

class TodosController < ApplicationController
  attr_reader :todo

  before_action :fetch_todo, only: %i[show update destroy]

  rescue_from ActiveRecord::RecordNotFound, with: :render_not_found
  rescue_from ActiveRecord::RecordInvalid, with: :render_bad_request

  def index
    todos = Todo.all
    render json: todos
  end

  def show
    render json: todo
  end

  def create
    new_todo = Todo.create!(todo_params)
    render json: new_todo, status: :created
  end

  def update
    todo.update!(todo_params)
    render json: todo
  end

  def destroy
    todo.destroy!
    head :no_content
  end

  private

  def fetch_todo
    @todo = Todo.find(params[:id])
  end

  def todo_params
    params.permit(:title, :completed)
  end

  def render_not_found
    render json: { error: 'not found' }, status: :not_found
  end

  def render_bad_request
    render json: { error: 'bad request' }, status: :bad_request
  end
end
```

# Routing

```ruby
# config/routes.rb

Rails.application.routes.draw do
  root to: 'healthcheck#index'
  get :healthcheck, to: 'healthcheck#index'

  resources :todos
end
```
