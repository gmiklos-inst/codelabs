package com.instructure.bp.codelabs.repository

import com.instructure.bp.codelabs.entity.TodoItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TodoItemRepository : JpaRepository<TodoItem, String>