reference repo: https://github.com/mikkabouzu/instructure-rails-workshop

# STEPS

## New Rails app

* `$ gem install rails -v 5.2.3`
* `$ rails new todo_api -d postgresql -M -T -C --api`
* `$ cd todo_api`
* add `rspec-rails` to Gemfile (dev & test)
* `$ bin/bundle'
* `$ bin/rails generate rspec:install`
* `$ bin/rake db:create`
* add `Procfile`

## Healthcheck endpoint, heroku deploy

* routing tests
* `$ bin/rails generate controller healthcheck`
* routing
* controller tests
* controller body

* `$ heroku create`
* `$ git push heroku master`
