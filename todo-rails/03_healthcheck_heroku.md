---
title: Healthcheck, Heroku
parent: Rails
nav_order: 3
---

Let's create a `/healtcheck` endpoint so we can verify that our application is up and running.
Let's start with some tests:

```ruby
# spec/requests/healtcheck_spec.rb

require 'rails_helper'

RSpec.describe 'healthcheck' do
  it 'responds with http 200' do
    get '/healthcheck'
    expect(response).to have_http_status(:ok)
  end

  it 'responds with {"success": true}' do
    get '/healthcheck'
    response_body = JSON.parse(response.body)
    expect(response_body).to eql('success' => true)
  end
end
```

We need to create a controller:

```ruby
# app/controllers/healthcheck_controller.rb

class HealthcheckController < ApplicationController
  def index
    render json: { success: true }
  end
end
```

And add a route:

```ruby
# config/routes.rb

Rails.application.routes.draw do
  get :healthcheck, to: 'healthcheck#index'
end
```

Let's make our default route `/` go to `/healthcheck`! We can write a routing spec for this:

```ruby
# spec/routing/healthcheck_spec.rb

RSpec.describe 'healthcheck' do
  it 'is routed to /' do
    expect(get: '/').to route_to(controller: 'healthcheck', action: 'index')
  end

  it 'is routed to /healthcheck' do
    expect(get: '/healthcheck').to route_to(controller: 'healthcheck', action: 'index')
  end
end
```

Add the default route:

```ruby
# config/routes.rb

Rails.application.routes.draw do
  # [...]
  root to: 'healthcheck#index'
  # [...]
end
```

Now we can deploy the app to Heroku:

```bash
$ heroku create
$ git push heroku master
```
