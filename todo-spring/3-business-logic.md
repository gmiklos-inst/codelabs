---
title: Business logic
parent: Spring Boot & Kotlin
nav_order: 4
---

# Business logic

## DB layer classes
Let's create our `Entity` class, which is going to be similar to the already existing `DTO`.
```kotlin
data class TodoItem(
        val id: String = "",
        val title: String,
        val completed: Boolean = false,
        val createdAt: OffsetDateTime? = null,
        val updatedAt: OffsetDateTime? = null,
        val completedAt: OffsetDateTime? = null)
```

We need a `TodoItemRepository` interface with a corresponding dummy implementation:
```kotlin
interface TodoItemRepository {
    fun findAll(): List<TodoItem>
    fun getOne(id: String): TodoItem
    fun save(todoItem: TodoItem): TodoItem
    fun existsById(id: String): Boolean
    fun deleteById(id: String)
}

@Repository
class DummyTodoItemRepository : TodoItemRepository {
    ...
}

```

## Implementing the TodoItemService

We can start working on the real implementation of the service, each method at a time. 
We need to think of these usecases:
* The `Service` should convert between entities and dtos, since the `Controller` uses dtos, while the `Repository` understands entities.
* The `Service` should communicate with the `Repository` and return values should come from there as well.
* The `Service` should handle cases where the `Repository` returns null, because it cannot find the `Entitiy` with the given id.
* The `Service` should manage the `createdAt`, `updatedAt` and `completedAt` properties of the `Entity`.

For easy conversion from `Dto` to `Entity` and vice versa, you can create `toEntity()` and `toDto()` methods for the data classes. 
For this purpose, you can use `companion object`s, extension methods or simple utility methods.
If in trouble, consult the checkpoint implementation.

## Checkpoint acceptance criteria
By the end of this checkpoint, you should have a `TodoItemService` implementation that handles all business use cases.