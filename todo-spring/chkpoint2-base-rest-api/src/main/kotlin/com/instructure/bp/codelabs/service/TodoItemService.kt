package com.instructure.bp.codelabs.service

import com.instructure.bp.codelabs.dto.TodoItemDto
import org.springframework.stereotype.Service
import java.util.*

@Service
class TodoItemService {

    private var todoItems = listOf(
            TodoItemDto("1", "title1", true),
            TodoItemDto("2", "title2", true),
            TodoItemDto("3", "title3", false),
            TodoItemDto("4", "title4", true)
    )

    fun getAllTodoItems() = todoItems.sortedBy { it.id }

    fun createTodoItem(todoItemDto: TodoItemDto): TodoItemDto {
        val dto = todoItemDto.copy(id = nextId())
        todoItems = todoItems + dto
        return dto
    }

    fun updateTodoItem(id: String, todoItemDto: TodoItemDto): TodoItemDto {
        val updatedTodoItem = todoItemDto.copy(id = id)
        todoItems = todoItems.filter { it.id != id } + updatedTodoItem
        return updatedTodoItem
    }

    fun deleteTodoItem(id: String): String {
        todoItems = todoItems.filter { it.id != id }
        return id
    }

    private fun nextId() = UUID.randomUUID().toString()
}