---
title: Pimp my APP
parent: Spring Boot & Kotlin
nav_order: 7
---

# Pimp my App

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

## Filtering

To enable arbitrary filtering on `Entity` fields, the easiest way to go is with `Specification`s.

* As a first step, add `implementation 'net.kaczmarzyk:specification-arg-resolver:2.1.1'` as implementation dependency.

* Then add the following argument to the `Controller`'s `getAllTodoItems` method, next to the `Pageable` parameter:

```kotlin
@And(value = [
     Spec(path = "title", spec = Like::class),
     Spec(path = "completed", spec = Equal::class)])
     todoItemSpec: Specification<TodoItem>?
```

* Define the following function in `TodoItemRepository`:

```kotlin
fun findAll(todoItemSpec: Specification<TodoItem>?, pageable: Pageable): Page<TodoItem>
```

* Make sure to send the `Specification` all the way down to the `Repository`
* Update your tests, because they will start failing
* Make the following changes in `WebConfiguration`
    * extend from `WebMvcConfigurer`
    * add the following method that will resolve our `Specification`s


```kotlin
override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
  argumentResolvers.add(SpecificationArgumentResolver())
}
```

Btw, [interesting reading](https://blog.tratif.com/2017/11/23/effective-restful-search-api-in-spring/) on this topic.

## Error handling

We would love to handle `Exception`s that occur in our `Controller`s or in other components they call
and have a unified/controlled Error response to that. Fortunately, we can do so with the `@ControllerAdvice` and `@ExceptionHandler` annotations!

* Our error message format is going to look like this:

```kotlin
data class ErrorResponseDto(val errors: List<ErrorDto>)

data class ErrorDto(val code: Int, val title: String)
```

* Create a new class that will look something like this:

```kotlin
@ControllerAdvice
class ControllerExceptionHandler {

   @ExceptionHandler(Exception::class)
   fun handleException(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponseDto> = 
           TODO("implement me")

   private fun responseEntity(status: HttpStatus, errorCode: Int, errorMessage: String) =
           ResponseEntity
                   .status(status)
                   .body(ErrorResponseDto(listOf(
                           ErrorDto(errorCode, errorMessage)
                   )))
}
```

* Think of what kind of exceptions can occur
* Discuss what tests should be written
* For inspiration, you have a look at the `ControllerExceptionHandlerTest` and `ControllerExceptionHandler` classes

## Securing with an API token