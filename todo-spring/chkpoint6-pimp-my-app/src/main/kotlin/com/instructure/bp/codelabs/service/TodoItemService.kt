package com.instructure.bp.codelabs.service

import com.instructure.bp.codelabs.dto.BaseTodoItemDto
import com.instructure.bp.codelabs.entity.TodoItem
import com.instructure.bp.codelabs.entity.toDto
import com.instructure.bp.codelabs.repository.TodoItemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.OffsetDateTime
import javax.persistence.EntityNotFoundException

@Service
class TodoItemService {

    @Autowired
    private lateinit var todoItemRepository: TodoItemRepository

    @Autowired
    private lateinit var clock: Clock

    fun getAllTodoItems(pageable: Pageable) =
            todoItemRepository
                    .findAll(pageable)
                    .map { it.toDto() }

    fun getTodoItem(id: String) = todoItemRepository.getOne(id).toDto()

    fun createTodoItem(baseTodoItemDto: BaseTodoItemDto) =
            todoItemRepository.save(
                    baseTodoItemDto.toEntity()
            ).toDto()

    fun updateTodoItem(id: String, baseTodoItemDto: BaseTodoItemDto) =
            todoItemRepository.findById(id).orElseThrow {
                EntityNotFoundException()
            }.let { originalTodoItem ->
                todoItemRepository.save(originalTodoItem.updateWith(baseTodoItemDto))
            }.toDto()

    fun deleteTodoItem(id: String) {
        if (todoItemRepository.existsById(id)) {
            todoItemRepository.deleteById(id)
        } else throw EntityNotFoundException()
    }

    private fun BaseTodoItemDto.toEntity() = TodoItem(
            id = "",
            title = title,
            completed = completed,
            completedAt = completedDate(completed)
    )

    private fun TodoItem.updateWith(baseTodoItemDto: BaseTodoItemDto) =
            this.copy(
                    title = baseTodoItemDto.title,
                    completed = baseTodoItemDto.completed,
                    completedAt = completedDate(baseTodoItemDto.completed)
            )

    private fun completedDate(completed: Boolean) = if (completed) OffsetDateTime.now(clock) else null
}