---
title: GET /todos/:id
parent: Rails
nav_order: 4
---

In order to handle todos, we need to create a model. We can use one of the rails generators to do so:

```bash
$ bin/rails g model todo title:string completed_at:timestamp
```

This will generate the following files for us:

```
  invoke  active_record
  create    db/migrate/yyyymmddhhmmss_create_todos.rb
  create    app/models/todo.rb
  invoke    rspec
  create      spec/models/todo_spec.rb
  invoke      factory_bot
  create        spec/factories/todos.rb
```

Make two small additions to the migration:
* make `title` not nullable
* add the the default `false` value for `completed_at`

```ruby
# db/migrate/yyyymmddhhmmss_create_todos.rb

class CreateTodos < ActiveRecord::Migration[5.2]
  def change
    create_table :todos do |t|
      t.string :title, null: false # this will be a required field
      t.boolean :completed, default: false # specify the default value here
      t.timestamp :completed_at

      t.timestamps # created_at and updated_at will be provided by rails
    end
  end
end
```

Migrate the database:

```bash
$ bin/rails db:migrate
```

Let's check out the table created in psql:

```bash
$ bin/rails dbconsole
psql (9.6.12)
Type "help" for help.

todo_api_development=# \dt;
                List of relations
 Schema |         Name         | Type  |  Owner
--------+----------------------+-------+----------
 public | ar_internal_metadata | table | postgres
 public | schema_migrations    | table | postgres
 public | todos                | table | postgres
(3 rows)

todo_api_development=# \d todos;
                                      Table "public.todos"
    Column    |            Type             |                     Modifiers
--------------+-----------------------------+----------------------------------------------------
 id           | bigint                      | not null default nextval('todos_id_seq'::regclass)
 title        | character varying           | not null
 completed    | boolean                     | default false
 completed_at | timestamp without time zone |
 created_at   | timestamp without time zone | not null
 updated_at   | timestamp without time zone | not null
Indexes:
    "todos_pkey" PRIMARY KEY, btree (id)
```

Now we can can create some todos in the rails console:

```bash
$ bin/rails c
```

```ruby
Todo.create!(title: 'first todo')
# => <Todo id: 1, title: "first todo", completed: false, completed_at: nil, created_at: "2019-05-02 10:02:08", updated_at: "2019-05-02 10:02:08">
Todo.create!(title: 'second todo')
# => <Todo id: 2, title: "second todo", completed: false, completed_at: nil, created_at: "2019-05-02 10:02:13", updated_at: "2019-05-02 10:02:13">
Todo.create!(title: 'completed todo', completed: true, completed_at: Time.now)
# => <Todo id: 3, title: "completed todo", completed: true, completed_at: "2019-05-02 10:02:32", created_at: "2019-05-02 10:02:32", updated_at: "2019-05-02 10:02:32">

Todo.count
# => 3
Todo.all
# => #<ActiveRecord::Relation [#<Todo id: 1, title: "first todo", completed: false, completed_at: nil, created_at: "2019-05-02 10:02:08", updated_at: "2019-05-02 10:02:08">, #<Todo id: 2, title: "second todo", completed: false, completed_at: nil, created_at: "2019-05-02 10:02:13", updated_at: "2019-05-02 10:02:13">, #<Todo id: 3, title: "completed todo", completed: true, completed_at: "2019-05-02 10:02:32", created_at: "2019-05-02 10:02:32", updated_at: "2019-05-02 10:02:32">]>
Todo.where(completed: true)
# => #<ActiveRecord::Relation [#<Todo id: 3, title: "completed todo", completed: true, completed_at: "2019-05-02 10:02:32", created_at: "2019-05-02 10:02:32", updated_at: "2019-05-02 10:02:32">]>
Todo.where(completed: true).count
# => 1
```

Now that we have our model, we can create a controller and implement `GET /todos/:id`:

```ruby
# spec/routing/todos_spec.rb

RSpec.describe 'todos' do
  it 'GET todos/:id is routed to todos#show' do
    expect(get: '/todos/42').to route_to(controller: 'todos', action: 'show', id: '42')
  end
end
```

```ruby
# config/routes.rb

Rails.application.routes.draw do
  # [...]
  get 'todos/:id', to: 'todos#show'
  # [...]
end
```

```ruby
# spec/requests/todos_spec.rb

require 'rails_helper'

RSpec.describe 'todos' do
  describe 'GET /todos/:id' do
    context 'when there is a todo with the specified id' do
      let!(:todo) { create(:todo) }

      it 'responds with http 200' do
        get "/todos/#{todo.id}"
        expect(response).to have_http_status(:ok)
      end

      it 'responds with the todo as json' do
        get "/todos/#{todo.id}"
        expect(response.body).to eql(todo.to_json)
      end
    end

    context 'when there is no todo with the specified id' do
      it 'responds with http 404' do
        get '/todos/42'
        expect(response).to have_http_status(:not_found)
      end

      it 'responds with an error' do
        get '/todos/42'
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
  def show
    todo = Todo.find(params[:id])
    render json: todo
  rescue ActiveRecord::RecordNotFound
    render json: { error: 'not found' }, status: :not_found
  end
end
```

We have to run the migrations by hand when deploying to heroku:

```bash
$ git push heroku master
$ heroku run 'bin/rails db:migrate'
```
