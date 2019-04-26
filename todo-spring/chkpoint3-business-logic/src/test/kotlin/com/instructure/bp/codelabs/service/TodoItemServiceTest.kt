package com.instructure.bp.codelabs.service

import com.instructure.bp.codelabs.dto.SaveTodoItemDto
import com.instructure.bp.codelabs.dto.TodoItemDto
import com.instructure.bp.codelabs.entity.TodoItem
import com.instructure.bp.codelabs.entity.toDto
import com.instructure.bp.codelabs.exception.TodoItemNotFound
import com.instructure.bp.codelabs.repository.TodoItemRepository
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Clock
import java.time.OffsetDateTime
import java.time.Instant
import java.time.ZoneId


@ExtendWith(MockitoExtension::class)
class TodoItemServiceTest {
    @Mock
    private lateinit var todoItemRepository: TodoItemRepository

    @Mock
    private lateinit var clock: Clock

    @InjectMocks
    private lateinit var todoItemService: TodoItemService

    private lateinit var createdAt: OffsetDateTime
    private lateinit var updatedAt: OffsetDateTime
    private lateinit var completedAt: OffsetDateTime

    @BeforeEach
    fun init() {
        clock.setup()
        createdAt = OffsetDateTime.now(clock)
        updatedAt = createdAt.plusMinutes(10)
        completedAt = updatedAt.plusHours(1)
    }

    @Test
    fun `getAllTodoItems returns result from repository as DTO`() {
        val entities = listOf(
                TodoItem("id1", "title1", false, createdAt, updatedAt, completedAt),
                TodoItem("id2", "title2", true),
                TodoItem("id3", "title3")
        )
        `when`(todoItemRepository.findAll()).thenReturn(entities)

        val expectedDtos = listOf(
                TodoItemDto("id1", "title1", false, createdAt, updatedAt, completedAt),
                TodoItemDto("id2", "title2", true),
                TodoItemDto("id3", "title3")
        )

        val actualDtos = todoItemService.getAllTodoItems()

        actualDtos shouldBe expectedDtos
    }

    @Test
    fun `getTodoItem returns result from repository as DTO`() {
        val id = "id1"
        val entity = TodoItem(id, "title1", false, createdAt, updatedAt, completedAt)
        `when`(todoItemRepository.findOne(id)).thenReturn(entity)

        val expectedDto = TodoItemDto(id, "title1", false, createdAt, updatedAt, completedAt)

        val actualDto = todoItemService.getTodoItem(id)

        actualDto shouldBe expectedDto
    }

    @Test
    fun `getTodoItem throws exception when there is no Entity with such ID`() {
        shouldThrow<TodoItemNotFound> {
            todoItemService.getTodoItem("id")
        }
    }

    @Test
    fun `createTodoItem returns result from repository as DTO`() {
        val saveTodoItemRequest = SaveTodoItemDto("title")
        val entityToSave = TodoItem("", saveTodoItemRequest.title, false)
        val savedEntity = entityToSave.copy(id="uuid", createdAt = createdAt)
        `when`(todoItemRepository.save(entityToSave)).thenReturn(savedEntity)
        val expectedDto = savedEntity.toDto()

        val actualDto = todoItemService.createTodoItem(saveTodoItemRequest)

        actualDto shouldBe expectedDto
    }

    @Test
    fun `createTodoItem saves TodoItem completedAt if completed set to true`() {
        val saveTodoItemRequest = SaveTodoItemDto("title", true)
        val entityToSave = TodoItem("", saveTodoItemRequest.title, true, completedAt = OffsetDateTime.now(clock))
        val savedEntity = entityToSave.copy(id="uuid", createdAt = createdAt)
        `when`(todoItemRepository.save(entityToSave)).thenReturn(savedEntity)
        val expectedDto = savedEntity.toDto()

        val actualDto = todoItemService.createTodoItem(saveTodoItemRequest)

        actualDto shouldBe expectedDto
    }

    private fun Clock.setup(){
        `when`(instant()).thenReturn(Instant.parse("2007-12-03T10:15:30Z"))
        `when`(zone).thenReturn(ZoneId.of("+02:00"))
    }
}