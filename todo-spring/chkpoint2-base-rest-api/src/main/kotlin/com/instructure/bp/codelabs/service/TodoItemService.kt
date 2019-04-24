package com.instructure.bp.codelabs.service

import com.instructure.bp.codelabs.dto.SaveTodoItemDto
import com.instructure.bp.codelabs.dto.TodoItemDto
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

@Service
class TodoItemService {

    private var todoItems = listOf(
            TodoItemDto("1", "title1", true, OffsetDateTime.now()),
            TodoItemDto("2", "title2", true, OffsetDateTime.now()),
            TodoItemDto("3", "title3", false, OffsetDateTime.now()),
            TodoItemDto("4", "title4", true, OffsetDateTime.now())
    )

    fun getAllTodoItems() = todoItems.sortedBy { it.id }

    fun getTodoItem(id: String) = todoItems.first { it.id == id }

    fun createTodoItem(saveTodoItemDto: SaveTodoItemDto): TodoItemDto {
        val dto = TodoItemDto(
                nextId(),
                saveTodoItemDto.title,
                saveTodoItemDto.completed,
                OffsetDateTime.now())
        todoItems += dto
        return dto
    }

    fun updateTodoItem(id: String, todoItemDto: TodoItemDto): TodoItemDto {
        val currentDateTime = OffsetDateTime.now()
        val oldTodoItem = todoItems.first { it.id == id }
        val completed = !oldTodoItem.completed && todoItemDto.completed
        val completedAt = if(completed) currentDateTime else null

        val updatedTodoItem = todoItemDto.copy(
                id = id,
                updatedAt = currentDateTime,
                completedAt = completedAt
        )
        todoItems = todoItems.filter { it.id != id } + updatedTodoItem
        return updatedTodoItem
    }

    fun deleteTodoItem(id: String): String {
        todoItems = todoItems.filter { it.id != id }
        return id
    }

    private fun nextId() = UUID.randomUUID().toString()
}