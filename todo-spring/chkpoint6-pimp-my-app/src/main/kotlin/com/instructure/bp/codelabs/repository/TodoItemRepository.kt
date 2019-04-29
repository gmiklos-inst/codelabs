package com.instructure.bp.codelabs.repository

import com.instructure.bp.codelabs.entity.TodoItem
import org.springframework.data.domain.Page
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Pageable

@Repository
interface TodoItemRepository : JpaRepository<TodoItem, String> {
    fun findAll(todoItemSpec: Specification<TodoItem>?, pageable: Pageable): Page<TodoItem>
}