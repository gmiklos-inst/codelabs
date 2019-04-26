package com.instructure.bp.codelabs.controller

import com.instructure.bp.codelabs.dto.BaseTodoItemDto
import com.instructure.bp.codelabs.dto.TodoItemDto
import com.instructure.bp.codelabs.service.TodoItemService
import io.kotlintest.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.OffsetDateTime

@ExtendWith(MockitoExtension::class)
class TodoItemControllerTest {

    @Mock
    private lateinit var todoItemService: TodoItemService

    @InjectMocks
    private lateinit var todoItemController: TodoItemController

    private lateinit var todoItems: List<TodoItemDto>
    private lateinit var requestTodoItem: TodoItemDto

    @BeforeEach
    fun init() {
        requestTodoItem = TodoItemDto("", "title", false)
        todoItems = listOf(
                TodoItemDto("1", "title1", false),
                TodoItemDto("2", "title2", true),
                TodoItemDto("3", "title3", false)
        )
    }

    @Test
    fun `getTodoItems returns all the todoItems`() {
        `when`(todoItemService.getAllTodoItems()).thenReturn(todoItems)

        val actualTodoItems = todoItemController.getTodoItems()

        actualTodoItems shouldBe todoItems
    }

    @Test
    fun `getTodoItem returns the todoItem`() {
        val id = "id"
        val expectedTodoItem = TodoItemDto("id", "expectedTitle", true)
        `when`(todoItemService.getTodoItem(id)).thenReturn(expectedTodoItem)

        val actualTodoItem = todoItemController.getTodoItem(id)

        actualTodoItem shouldBe expectedTodoItem
    }

    @Test
    fun `createTodoItem returns the created todoItem`() {
        val saveTodoItem = BaseTodoItemDto("title", false)
        val expectedTodoItem = TodoItemDto("12345", saveTodoItem.title, saveTodoItem.completed, OffsetDateTime.now())
        `when`(todoItemService.createTodoItem(saveTodoItem)).thenReturn(expectedTodoItem)

        val actualResponse = todoItemController.addTodoItem(saveTodoItem)
        val expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body(expectedTodoItem)

        actualResponse shouldBe expectedResponse
    }

    @Test
    fun `updateTodoItem returns the updated todoItem`() {
        val id = "12345"
        val requestBaseTodoItem = BaseTodoItemDto("newTitle", false)
        val expectedTodoItem = TodoItemDto(id, requestBaseTodoItem.title, requestBaseTodoItem.completed)
        `when`(todoItemService.updateTodoItem(id, requestBaseTodoItem)).thenReturn(expectedTodoItem)

        val actualTodoItem = todoItemController.updateTodoItem(id, requestBaseTodoItem)

        actualTodoItem shouldBe  expectedTodoItem
    }

    @Test
    fun `deleteTodoItem deletes a todoItem`() {
        val id = "1"

        todoItemController.deleteTodoItem(id)

        verify(todoItemService).deleteTodoItem(id)
    }
}