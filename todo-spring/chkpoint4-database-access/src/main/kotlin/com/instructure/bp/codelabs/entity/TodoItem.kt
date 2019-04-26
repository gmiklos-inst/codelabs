package com.instructure.bp.codelabs.entity

import com.instructure.bp.codelabs.dto.TodoItemDto
import java.time.OffsetDateTime

data class TodoItem(
        val id: String = "",
        val title: String,
        val completed: Boolean = false,
        val createdAt: OffsetDateTime? = null,
        val updatedAt: OffsetDateTime? = null,
        val completedAt: OffsetDateTime? = null)

fun TodoItem.toDto(): TodoItemDto = TodoItemDto(
        id,
        title,
        completed,
        createdAt,
        updatedAt,
        completedAt
)