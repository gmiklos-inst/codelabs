---
title: POST /todos
parent: Rails
nav_order: 7
---

We need to implement `title` validation on the todo model:

```ruby
# spec/models/todo_spec.rb

RSpec.describe Todo, type: :model do
  describe 'validations' do
    it { is_expected.to validate_presence_of(:title) }
  end
end
```

```ruby
# app/models/todo.rb

class Todo < ApplicationRecord
  validates :title, presence: true
end
```

We also need to handle `completed_at` properly when completing or uncompleting a todo:

```ruby
# spec/factories/todos.rb

FactoryBot.define do
  factory :todo do
    title { 'MyString' }
    completed { false }

    trait :completed do
      completed { true }
      completed_at { '2019-05-03 00:36:38' }
    end

    trait :uncompleted do
      completed { false }
      completed_at { nil }
    end
  end
end
```

```ruby
# spec/models/todo.rb

RSpec.describe Todo, type: :model do
  include ActiveSupport::Testing::TimeHelpers

  # [...]

  describe 'completed_at' do
    context 'mark a todo completed' do
      context 'todo is completed' do
        subject { build(:todo, :completed) }

        it 'will not change completed_at' do
          expect { subject.update(completed: true) }.not_to(change { subject.completed_at })
        end
      end

      context 'todo is uncompleted' do
        subject { build(:todo, :uncompleted) }

        it 'will set completed_at to now' do
          freeze_time do
            expect { subject.update(completed: true) }.to change { subject.completed_at }.to(Time.current)
          end
        end
      end
    end

    context 'mark a todo uncompleted' do
      context 'todo is completed' do
        subject { build(:todo, :completed) }

        it 'will set completed_at to nil' do
          expect { subject.update(completed: false) }.to change { subject.completed_at }.to(nil)
        end
      end

      context 'todo is uncompleted' do
        subject { build(:todo, :uncompleted) }

        it 'will not change completed_at' do
          expect { subject.update(completed: false) }.not_to(change { subject.completed_at })
        end
      end
    end
  end
end
```

```ruby
# app/models/todo.rb
class Todo < ApplicationRecord
  # [...]

  before_validation :handle_completed_at

  private

  def handle_completed_at
    if completed?
      self.completed_at ||= Time.zone.now
    else
      self.completed_at = nil
    end
  end
end
```

And the endpoint:

```ruby
# spec/routing/todos_spec.rb

RSpec.describe 'todos' do
  # [...]

  it 'POST todos/ is routed to todos#create' do
    expect(post: '/todos').to route_to(controller: 'todos', action: 'create')
  end
end
```

```ruby
# config/routes.rb
Rails.application.routes.draw do
  # [...]

  post 'todos', to: 'todos#create'
end
```

```ruby
# spec/requests/todos_spec.rb

RSpec.describe 'todos' do
  # [...]

  describe 'POST /todos' do
    context 'with valid parameters' do
      let(:valid_params) { { title: 'just do it' } }

      it 'responds with http 201' do
        post '/todos', params: valid_params
        expect(response).to have_http_status(:created)
      end

      it 'creates a todo' do
        expect { post '/todos', params: valid_params }.to change(Todo, :count).by(1)
      end

      it 'responds with the newly created todo' do
        post '/todos', params: valid_params
        expect(response.body).to eql(Todo.last.to_json)
      end
    end

    context 'with invalid parameters' do
      let(:invalid_params) { { something: 'irrelevant' } }

      it 'responds with http bad request' do
        post '/todos', params: invalid_params
        expect(response).to have_http_status(:bad_request)
      end

      it 'responds with an error' do
        post '/todos', params: invalid_params
        response_body = JSON.parse(response.body)
        expect(response_body).to eql('error' => 'bad request')
      end
    end
  end
end
```

```ruby
# app/controllers/todos_controller.rb
class TodosController < ApplicationController
  # [...]

  def create
    todo = Todo.create!(todo_params)
    render json: todo, status: :created
  rescue ActiveRecord::RecordInvalid
    render json: { error: 'bad request' }, status: :bad_request
  end

  private

  def todo_params
    params.permit(:title, :completed)
  end
end
```
