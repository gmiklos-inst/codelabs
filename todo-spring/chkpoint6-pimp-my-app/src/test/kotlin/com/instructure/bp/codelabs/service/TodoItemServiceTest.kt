package com.instructure.bp.codelabs.service

import com.instructure.bp.codelabs.dto.BaseTodoItemDto
import com.instructure.bp.codelabs.dto.TodoItemDto
import com.instructure.bp.codelabs.entity.TodoItem
import com.instructure.bp.codelabs.entity.toDto
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
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import java.time.Clock
import java.time.OffsetDateTime
import java.time.Instant
import java.time.ZoneId
import java.util.*
import javax.persistence.EntityNotFoundException


@ExtendWith(MockitoExtension::class)
class TodoItemServiceTest {
    @Mock
    private lateinit var todoItemRepository: TodoItemRepository

    @Mock
    private lateinit var clock: Clock

    @InjectMocks
    private lateinit var todoItemService: TodoItemService

    private lateinit var now: OffsetDateTime
    private lateinit var createdAt: OffsetDateTime
    private lateinit var updatedAt: OffsetDateTime
    private lateinit var completedAt: OffsetDateTime

    @BeforeEach
    fun init() {
        clock.setup()
        now = OffsetDateTime.now(clock)
        createdAt = now
        updatedAt = createdAt.plusMinutes(10)
        completedAt = updatedAt.plusHours(1)
    }

    @Test
    fun `getAllTodoItems returns result from repository as DTO`() {
        val specification = mock(Specification::class.java) as Specification<TodoItem>
        val pageable = mock(Pageable::class.java)
        val entities = listOf(
                TodoItem("id1", "title1", false, createdAt, updatedAt, completedAt),
                TodoItem("id2", "title2", true),
                TodoItem("id3", "title3")
        )
        `when`(todoItemRepository.findAll(specification, pageable)).thenReturn(PageImpl(entities))

        val expectedDtos = listOf(
                TodoItemDto("id1", "title1", false, createdAt, updatedAt, completedAt),
                TodoItemDto("id2", "title2", true),
                TodoItemDto("id3", "title3")
        )

        val actualDtos = todoItemService.getAllTodoItems(specification, pageable).toList()

        actualDtos shouldBe expectedDtos
    }

    @Test
    fun `getTodoItem returns result from repository as DTO`() {
        val id = "id1"
        val entity = TodoItem(id, "title1", false, createdAt, updatedAt, completedAt)
        `when`(todoItemRepository.getOne(id)).thenReturn(entity)

        val expectedDto = TodoItemDto(id, "title1", false, createdAt, updatedAt, completedAt)

        val actualDto = todoItemService.getTodoItem(id)

        actualDto shouldBe expectedDto
    }

    @Test
    fun `createTodoItem returns result from repository as DTO`() {
        val saveTodoItemRequest = BaseTodoItemDto("title")
        val entityToSave = TodoItem("", saveTodoItemRequest.title, false)
        val savedEntity = entityToSave.copy(id = "uuid", createdAt = createdAt)
        `when`(todoItemRepository.save(entityToSave)).thenReturn(savedEntity)
        val expectedDto = savedEntity.toDto()

        val actualDto = todoItemService.createTodoItem(saveTodoItemRequest)

        actualDto shouldBe expectedDto
    }

    @Test
    fun `createTodoItem saves TodoItem completedAt if completed set to true`() {
        val saveTodoItemRequest = BaseTodoItemDto("title", true)
        val entityToSave = TodoItem("", saveTodoItemRequest.title, true, completedAt = now)
        val savedEntity = entityToSave.copy(id = "uuid", createdAt = createdAt)
        `when`(todoItemRepository.save(entityToSave)).thenReturn(savedEntity)
        val expectedDto = savedEntity.toDto()

        val actualDto = todoItemService.createTodoItem(saveTodoItemRequest)

        actualDto shouldBe expectedDto
    }

    @Test
    fun `updateTodoItem saves TodoItem via repository`() {
        val id = "id"
        val dto = BaseTodoItemDto("title1", false)
        val entityToUpdateWith = TodoItem(id, dto.title, dto.completed)
        val updatedEntity = entityToUpdateWith.copy(id = id, updatedAt = updatedAt.plusHours(2))
        `when`(todoItemRepository.findById(id)).thenReturn(Optional.of(entityToUpdateWith))
        `when`(todoItemRepository.save(entityToUpdateWith)).thenReturn(updatedEntity)

        val expectedDto = updatedEntity.toDto()

        val actualDto = todoItemService.updateTodoItem(id, dto)

        actualDto shouldBe expectedDto
    }

    @Test
    fun `updateTodoItem updates completedAt if todoItem is completed`() {
        val id = "id"
        val dto = BaseTodoItemDto("title1", true)
        val entityToBeUpdated = TodoItem(id, dto.title, false)
        val update = TodoItem(id, dto.title, true, completedAt = now)
        val updateResult = entityToBeUpdated.copy(id = id, updatedAt = updatedAt.plusHours(2))
        `when`(todoItemRepository.findById(id)).thenReturn(Optional.of(entityToBeUpdated))
        `when`(todoItemRepository.save(update)).thenReturn(updateResult)

        val expectedDto = updateResult.toDto()

        val actualDto = todoItemService.updateTodoItem(id, dto)

        actualDto shouldBe expectedDto
    }

    @Test
    fun `updateTodoItem throws exception if entity not found`() {
        val missingId = "not here"
        `when`(todoItemRepository.findById(missingId)).thenReturn(Optional.empty())

        shouldThrow<EntityNotFoundException> {
            todoItemService.updateTodoItem(missingId, BaseTodoItemDto(""))
        }
    }

    @Test
    fun `deleteTodoItem deletes todoItem via repository`() {
        val id = "id"
        `when`(todoItemRepository.existsById(id)).thenReturn(true)
        todoItemService.deleteTodoItem(id)

        verify(todoItemRepository).deleteById(id)
    }

    @Test
    fun `deleteTodoItem throws exception if entity not found`() {
        val missingId = "not here"
        `when`(todoItemRepository.existsById(missingId)).thenReturn(false)

        shouldThrow<EntityNotFoundException> {
            todoItemService.deleteTodoItem(missingId)
        }
    }

    private fun Clock.setup() {
        `when`(instant()).thenReturn(Instant.parse("2007-12-03T10:15:30Z"))
        `when`(zone).thenReturn(ZoneId.of("+02:00"))
    }
}