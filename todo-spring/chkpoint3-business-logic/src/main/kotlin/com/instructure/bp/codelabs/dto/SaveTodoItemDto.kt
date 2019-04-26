package com.instructure.bp.codelabs.dto

data class SaveTodoItemDto(
        val title: String,
        val completed: Boolean=false)