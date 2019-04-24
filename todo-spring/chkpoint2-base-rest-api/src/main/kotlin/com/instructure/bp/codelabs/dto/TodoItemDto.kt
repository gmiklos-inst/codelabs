package com.instructure.bp.codelabs.dto

import java.time.OffsetDateTime

data class TodoItemDto(
        val id: String = "",
        val title: String,
        val completed: Boolean = false,
        val createdAt: OffsetDateTime? = null,
        val updatedAt: OffsetDateTime? = null,
        val completedAt: OffsetDateTime? = null)