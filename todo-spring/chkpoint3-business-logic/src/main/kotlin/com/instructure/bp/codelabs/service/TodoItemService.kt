package com.instructure.bp.codelabs.service

import com.instructure.bp.codelabs.dto.SaveTodoItemDto
import com.instructure.bp.codelabs.dto.TodoItemDto
import com.instructure.bp.codelabs.entity.TodoItem
import com.instructure.bp.codelabs.entity.toDto
import com.instructure.bp.codelabs.exception.TodoItemNotFound
import com.instructure.bp.codelabs.repository.TodoItemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.OffsetDateTime

@Service
class TodoItemService {

    companion object {
        val TODO_ITEM = TodoItemDto("1", "title1", true, OffsetDateTime.now())
        val ID = TODO_ITEM.id
    }

    @Autowired
    private lateinit var todoItemRepository: TodoItemRepository

    @Autowired
    private lateinit var clock: Clock

    fun getAllTodoItems() = todoItemRepository.findAll().map { it.toDto() }

    fun getTodoItem(id: String) = todoItemRepository.findOne(id)?.toDto()
            ?: throw TodoItemNotFound(id)

    fun createTodoItem(saveTodoItemDto: SaveTodoItemDto) =
            todoItemRepository.save(
                    saveTodoItemDto.toEntity()
            ).toDto()

    fun updateTodoItem(id: String, todoItemDto: TodoItemDto) = TODO_ITEM

    fun deleteTodoItem(id: String) = ID

    private fun SaveTodoItemDto.toEntity() = TodoItem(
            title = title,
            completed = completed,
            completedAt = if (completed) OffsetDateTime.now(clock) else null
    )
}