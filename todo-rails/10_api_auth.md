---
title: API authentication
parent: Rails
nav_order: 10
---

Add the `dotenv-rails` gem to the `Gemfile` as a dev & test dependency.

```ruby
# config/application.rb

module TodoApi
  class Application < Rails::Application
    # [...]

    config.api_key = ENV.fetch('API_KEY')
  end
end
```

```ruby
# spec/requests/todos_unauthorized_spec.rb

require 'rails_helper'

RSpec.describe 'todos unauthorized' do
  describe 'GET /todos' do
    it 'responds with http 401' do
      get '/todos'
      expect(response).to have_http_status(:unauthorized)
    end
  end

  describe 'POST /todos' do
    it 'responds with http 401' do
      post '/todos'
      expect(response).to have_http_status(:unauthorized)
    end
  end

  describe 'GET /todos/:id' do
    it 'responds with http 401' do
      get '/todos/42'
      expect(response).to have_http_status(:unauthorized)
    end
  end

  describe 'PUT /todos/:id' do
    it 'responds with http 401' do
      put '/todos/42'
      expect(response).to have_http_status(:unauthorized)
    end
  end

  describe 'DELETE /todos/:id' do
    it 'responds with http 401' do
      delete '/todos/42'
      expect(response).to have_http_status(:unauthorized)
    end
  end
end
```

```ruby
# spec/requests/todos_authorized_spec.rb (renamed from spec/requests/todos_spec.rb)

require 'rails_helper'

RSpec.describe 'todos authorized' do
  let(:api_key) { 'some_key' }
  let(:headers) { { 'X_API_KEY' => api_key } }

  before { allow(Rails.configuration).to receive(:api_key).and_return(api_key) }

  describe 'GET /todos' do
    context 'when there are no todos' do
      it 'responds with http 200' do
        get '/todos', headers: headers
        expect(response).to have_http_status(:ok)
      end

      it 'responds with an empty array as json' do
        get '/todos', headers: headers
        response_body = JSON.parse(response.body)
        expect(response_body).to eql([])
      end
    end

    context 'when there are some todos' do
      let!(:todos) { create_list(:todo, 3) }

      it 'responds with http 200' do
        get '/todos', headers: headers
        expect(response).to have_http_status(:ok)
      end

      it 'responds with the todos in an array' do
        get '/todos', headers: headers
        response_body = JSON.parse(response.body)
        expect(response_body.count).to eq(todos.count)
      end
    end
  end

  describe 'POST /todos' do
    context 'with valid parameters' do
      let(:valid_params) { { title: 'just do it' } }

      it 'responds with http 201' do
        post '/todos', params: valid_params, headers: headers
        expect(response).to have_http_status(:created)
      end

      it 'creates a todo' do
        expect { post '/todos', params: valid_params, headers: headers }.to change(Todo, :count).by(1)
      end

      it 'responds with the newly created todo' do
        post '/todos', params: valid_params, headers: headers
        expect(response.body).to eql(Todo.last.to_json)
      end
    end

    context 'with invalid parameters' do
      let(:invalid_params) { { something: 'irrelevant' } }

      it 'responds with http bad request' do
        post '/todos', params: invalid_params, headers: headers
        expect(response).to have_http_status(:bad_request)
      end

      it 'responds with an error' do
        post '/todos', params: invalid_params, headers: headers
        response_body = JSON.parse(response.body)
        expect(response_body).to eql('error' => 'bad request')
      end
    end
  end

  describe 'GET /todos/:id' do
    context 'when there is a todo with the specified id' do
      let!(:todo) { create(:todo) }

      it 'responds with http 200' do
        get "/todos/#{todo.id}", headers: headers
        expect(response).to have_http_status(:ok)
      end

      it 'responds with the todo as json' do
        get "/todos/#{todo.id}", headers: headers
        expect(response.body).to eql(todo.to_json)
      end
    end

    context 'when there is no todo with the specified id' do
      it 'responds with http 404' do
        get '/todos/42', headers: headers
        expect(response).to have_http_status(:not_found)
      end

      it 'responds with an error' do
        get '/todos/42', headers: headers
        response_body = JSON.parse(response.body)
        expect(response_body).to eql('error' => 'not found')
      end
    end
  end

  describe 'PUT /todos/:id' do
    context 'when there is a todo with the specified id' do
      let!(:todo) { create(:todo, title: 'some todo') }

      context 'with valid parameters' do
        let(:valid_params) { { title: 'just do it' } }

        it 'responds with http 200' do
          put "/todos/#{todo.id}", params: valid_params, headers: headers
          expect(response).to have_http_status(:ok)
        end

        it 'updates the todo' do
          expect { put "/todos/#{todo.id}", params: valid_params, headers: headers }.to \
            change { todo.reload.title }.to(valid_params[:title])
        end

        it 'responds with the updated todo' do
          put "/todos/#{todo.id}", params: valid_params, headers: headers
          expect(response.body).to eql(todo.reload.to_json)
        end
      end

      context 'with invalid parameters' do
        let(:invalid_params) { { title: '' } }

        it 'responds with http bad request' do
          put "/todos/#{todo.id}", params: invalid_params, headers: headers
          expect(response).to have_http_status(:bad_request)
        end

        it 'responds with an error' do
          put "/todos/#{todo.id}", params: invalid_params, headers: headers
          response_body = JSON.parse(response.body)
          expect(response_body).to eql('error' => 'bad request')
        end
      end
    end

    context 'when there is no todo with the specified id' do
      it 'responds with http 404' do
        put '/todos/42', params: { title: 'just do it' }, headers: headers
        expect(response).to have_http_status(:not_found)
      end

      it 'responds with an error' do
        put '/todos/42', params: { title: 'just do it' }, headers: headers
        response_body = JSON.parse(response.body)
        expect(response_body).to eql('error' => 'not found')
      end
    end
  end

  describe 'DELETE /todos/:id' do
    context 'when there is a todo with the specified id' do
      let!(:todo) { create(:todo) }

      it 'responds with http 204' do
        delete "/todos/#{todo.id}", headers: headers
        expect(response).to have_http_status(:no_content)
      end

      it 'deletes the todo' do
        expect { delete "/todos/#{todo.id}", headers: headers }.to change { Todo.exists?(todo.id) }.from(true).to(false)
      end
    end

    context 'when there is no todo with the specified id' do
      it 'responds with http 404' do
        delete '/todos/42', headers: headers
        expect(response).to have_http_status(:not_found)
      end

      it 'responds with an error' do
        delete '/todos/42', headers: headers
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

  API_KEY_HEADER = 'X_API_KEY'
  before_action :check_api_key

  # [...]

  private

  def check_api_key
    return if request.headers[API_KEY_HEADER] == Rails.configuration.api_key

    head :unauthorized
  end

  # [...]
end
```

Don't forget to set the `API_KEY` env var on Heroku!
