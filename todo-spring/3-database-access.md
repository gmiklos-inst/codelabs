# Database access

## DB Dependencies
add implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
and runtimeOnly 'org.postgresql:postgresql'

## TodoItemDto
Let's create the Dto class that represents a TodoItem that can be (de)serialized from/into JSON format.
This `TodoItemDto` should have 3 fiels:
* a `String` `id` field
* a `String` `title` field
* a `Boolean` `completed` field

Please note that using a data class can be useful here. Also, we can use Jackson annotations on our `Dto` if there's a need for that.

## TodoItem and TodoItemRepository

At this point, we are not really concerned with database access yet.

As for the `Entity` class, we need a similar data class like with `TodoItemDto`

For the `Repository`, we need an interface like this:
```kotlin
interface TodoItemRepository {
    fun findAll(): List<TodoItem>
}
```

For easy conversion from `Dto` to `Entity` and vice versa, you can create `toEntity()` and `toDto()` methods for the data classes. It is up to you if you use `companion object`s or extension methods. Also, don't forget to use TDD and write your tests first.
