package com.instructure.bp.codelabs.repository

import com.instructure.bp.codelabs.entity.TodoItem

interface TodoItemRepository {
    fun findAll(): List<TodoItem>

    fun getOne(id: String): TodoItem

    fun save(todoItem: TodoItem): TodoItem

    fun exists(id: String): Boolean

    fun deleteById(id: String)
}