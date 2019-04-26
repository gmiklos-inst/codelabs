package com.instructure.bp.codelabs.controller

import com.instructure.bp.codelabs.dto.BaseTodoItemDto
import com.instructure.bp.codelabs.entity.TodoItem
import com.instructure.bp.codelabs.repository.TodoItemRepository
import io.kotlintest.shouldBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoItemControllerIntegrationTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var todoItemRepository: TodoItemRepository

    lateinit var persistedEntities: List<TodoItem>

    @BeforeEach
    fun init() {
        persistedEntities = todoItemRepository.saveAll(listOf(
                TodoItem(title = "title1"),
                TodoItem(title = "title2"),
                TodoItem(title = "title3", completed = true)
        ))
    }

    @AfterEach
    fun tearDown() {
        todoItemRepository.deleteAll()
    }

    @Test
    fun `test GET all todoItems`() {
        restTemplate.exchange(
                "/todos",
                HttpMethod.GET,
                null,
                Any::class.java).statusCodeValue shouldBe 200
    }

    @Test
    fun `test GET single todoItem`() {
        restTemplate.exchange(
                "/todos/${persistedEntities.first().id}",
                HttpMethod.GET,
                null,
                Any::class.java).statusCodeValue shouldBe 200
    }

    @Test
    fun `test POST todoItem`() {
        restTemplate.exchange(
                "/todos",
                HttpMethod.POST,
                HttpEntity(BaseTodoItemDto("title", true)),
                Any::class.java).statusCodeValue shouldBe 201
    }

    @Test
    fun `test PUT todoItem`() {
        restTemplate.exchange(
                "/todos/${persistedEntities.first().id}",
                HttpMethod.PUT,
                HttpEntity(BaseTodoItemDto("title", true)),
                Any::class.java).statusCodeValue shouldBe 200
    }

    @Test
    fun `test DELETE todoItem`() {
        restTemplate.exchange(
                "/todos/${persistedEntities.first().id}",
                HttpMethod.DELETE,
                null,
                Any::class.java).statusCodeValue shouldBe 204
    }
}