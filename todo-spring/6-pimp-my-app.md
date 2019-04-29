---
title: Pimp my API
parent: Spring Boot & Kotlin
nav_order: 7
---

# Pimp my App

## Error handling


## Filtering


## Paging
In order to make our API pageable, we need to make the following changes:
* Add a `Pageable` parameter to our `GET allTodoItems` endpoint.
* Make sure this `Pageable` object is sent all the way down to the repository.
* Call the `TodoItemRepository`'s `findAll` method with the `Pageable` instance.
* You can use the following query parameters in an API call: `page`, `size`, `sort`
    * For example: `?page=0&size=3&sort=createdAt,DESC`
* Make sure to update the tests

One thing to note:
When adding a `Pageable` parameter to a `Controller`'s method, you get a default `Pageable` even if it was not
defined in the API call itself as query params. In order to still support the 'query all' functionality
as a fallback, we need to customize how a `Pageable` is resolved from a request. We can customize it in our `WebConfiguration`
the following way:
```kotlin
@Bean
fun pageableResolverCustomizer() = PageableHandlerMethodArgumentResolverCustomizer { pageableResolver ->
    pageableResolver.setFallbackPageable(Pageable.unpaged())
}
``` 

## Using an API token