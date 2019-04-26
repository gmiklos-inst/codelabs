package com.instructure.bp.codelabs.controller

import com.instructure.bp.codelabs.dto.BaseTodoItemDto
import com.instructure.bp.codelabs.dto.TodoItemDto
import io.kotlintest.shouldBe
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
                "/todos/1",
                HttpMethod.GET,
                null,
                Any::class.java).statusCodeValue shouldBe 200
    }

    @Test
    fun `test POST todoItem`() {
        restTemplate.exchange(
                "/todos",
                HttpMethod.POST,
                HttpEntity(TodoItemDto("1", "title", true)),
                Any::class.java).statusCodeValue shouldBe 201
    }

    @Test
    fun `test PUT todoItem`() {
        restTemplate.exchange(
                "/todos/1",
                HttpMethod.PUT,
                HttpEntity(BaseTodoItemDto("title", true)),
                Any::class.java).statusCodeValue shouldBe 200
    }

    @Test
    fun `test DELETE todoItem`() {
        restTemplate.exchange(
                "/todos/2",
                HttpMethod.DELETE,
                null,
                Any::class.java).statusCodeValue shouldBe 204
    }
}