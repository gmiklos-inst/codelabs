package com.instructure.bp.codelabs.dto

data class TodoItemDto(
        val id: String="",
        val title: String,
        val completed: Boolean=false)