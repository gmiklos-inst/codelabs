package com.instructure.bp.codelabs.service

import com.instructure.bp.codelabs.dto.SaveTodoItemDto
import com.instructure.bp.codelabs.dto.TodoItemDto
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

@Service
class TodoItemService {

    companion object {
        val TODO_ITEMS = listOf(
                TodoItemDto("1", "title1", true, OffsetDateTime.now()),
                TodoItemDto("2", "title2", true, OffsetDateTime.now()),
                TodoItemDto("3", "title3", false, OffsetDateTime.now()),
                TodoItemDto("4", "title4", true, OffsetDateTime.now())
        )
        val TODO_ITEM = TODO_ITEMS.first()
        val ID = TODO_ITEM.id
    }

    fun getAllTodoItems() = TODO_ITEMS

    fun getTodoItem(id: String) = TODO_ITEM

    fun createTodoItem(saveTodoItemDto: SaveTodoItemDto) = TODO_ITEM

    fun updateTodoItem(id: String, todoItemDto: TodoItemDto) = TODO_ITEM

    fun deleteTodoItem(id: String) = ID

}