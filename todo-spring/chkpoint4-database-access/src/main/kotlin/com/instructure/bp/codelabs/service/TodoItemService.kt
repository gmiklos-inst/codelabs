package com.instructure.bp.codelabs.service

import com.instructure.bp.codelabs.dto.BaseTodoItemDto
import com.instructure.bp.codelabs.dto.TodoItemDto
import com.instructure.bp.codelabs.entity.TodoItem
import com.instructure.bp.codelabs.entity.toDto
import com.instructure.bp.codelabs.repository.TodoItemRepository
import org.springframework.beans.factory.annotation.Autowired
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

    fun getAllTodoItems() = todoItemRepository.findAll().map { it.toDto() }

    fun getTodoItem(id: String) = todoItemRepository.getOne(id).toDto()

    fun createTodoItem(baseTodoItemDto: BaseTodoItemDto) =
            todoItemRepository.save(
                    baseTodoItemDto.toEntity()
            ).toDto()

    fun updateTodoItem(id: String, baseTodoItemDto: BaseTodoItemDto) =
            if (todoItemRepository.existsById(id)) {
                todoItemRepository.save(
                        baseTodoItemDto.toEntity(id)
                ).toDto()
            } else throw EntityNotFoundException()


    fun deleteTodoItem(id: String) {
        if (todoItemRepository.existsById(id)) {
            todoItemRepository.deleteById(id)
        } else throw EntityNotFoundException()
    }

    private fun BaseTodoItemDto.toEntity(id: String = "") = TodoItem(
            id = id,
            title = title,
            completed = completed,
            completedAt = if (completed) OffsetDateTime.now(clock) else null
    )
}