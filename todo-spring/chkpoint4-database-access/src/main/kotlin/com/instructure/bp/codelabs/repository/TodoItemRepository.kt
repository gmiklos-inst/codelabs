package com.instructure.bp.codelabs.repository

import com.instructure.bp.codelabs.entity.TodoItem

interface TodoItemRepository {
    fun findAll(): List<TodoItem>

    fun findOne(id: String): TodoItem?

    fun save(todoItem: TodoItem): TodoItem
}