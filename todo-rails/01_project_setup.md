---
title: Project setup
parent: Rails
nav_order: 1
---

# Generate a new Rails project

```shell
$ rails new todo_api -T -d postgresql --api -C -M
$ cd todo_api
```
  * `-d` : Preconfigure for selected database (postgresql)
  * `-M` : Skip Action Mailer files
  * `-C` : Skip Action Cable files
  * `-T` : Skip test files (we are going to use `RSpec` instead of `Minitest`)

# Add some additional dependencies

* [rubocop](https://github.com/rubocop-hq/rubocop): static code analyzer and formatter
* [rspec-rails](https://github.com/rspec/rspec-rails): testing framework
* [shoulda_matchers](https://github.com/thoughtbot/shoulda-matchers): simple one-liner tests for common Rails functionality
* [factory_bot_rails](https://github.com/thoughtbot/factory_bot): test fixtures

add them to the Gemfile:

```ruby
# Gemfile

group :development, :test do
  gem 'rubocop'
  gem 'rspec-rails'
end

group :test do
  gem 'factory_bot_rails'
  gem 'shoulda-matchers'
  gem 'database_cleaner'
end
```

and run

```shell
$ bundle install
```

## rubocop

add `.rubocop.yml`:

```yaml
AllCops:
  TargetRubyVersion: 2.6
  Exclude:
    - 'db/schema.rb'
    - 'bin/*'

Metrics/LineLength:
  Max: 130

Rails:
  Enabled: true

Style/Documentation:
  Enabled: false
```

and run

```shell
$ rubocop -a
```

## rspec

```shell
$ bin/rails generate rspec:install
```
