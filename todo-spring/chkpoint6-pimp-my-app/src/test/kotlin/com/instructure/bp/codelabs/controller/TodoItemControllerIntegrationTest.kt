package com.instructure.bp.codelabs.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.instructure.bp.codelabs.dto.BaseTodoItemDto
import com.instructure.bp.codelabs.entity.TodoItem
import com.instructure.bp.codelabs.repository.TodoItemRepository
import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.tables.row
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.util.MultiValueMap

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TodoItemControllerIntegrationTest {

    private data class Request(
            val url: String,
            val httpMethod: HttpMethod,
            val body: Any? = null,
            val headers: MultiValueMap<String, String> = HttpHeaders())

    @Value("\${codelabs.security.apikey}")
    private lateinit var apiKey: String

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var objectMapper: ObjectMapper

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
    fun `integration tests with auth headers`() {
        forall(
                row(
                        Request(
                                "/todos",
                                HttpMethod.GET,
                                headers = authHeader()),
                        200
                ),
                row(
                        Request(
                                "/todos/${persistedEntities.first().id}",
                                HttpMethod.GET,
                                headers = authHeader()),
                        200
                ),
                row(
                        Request(
                                "/todos/",
                                HttpMethod.POST,
                                BaseTodoItemDto("title", true),
                                authHeader()),
                        201
                ),
                row(
                        Request(
                                "/todos/${persistedEntities.first().id}",
                                HttpMethod.PUT,
                                BaseTodoItemDto("title", true),
                                authHeader()),
                        200
                ),
                row(
                        Request(
                                "/todos/${persistedEntities.first().id}",
                                HttpMethod.DELETE,
                                headers = authHeader()),
                        204
                ),
                row(
                        Request(
                                "/todos",
                                HttpMethod.GET),
                        403
                ),
                row(
                        Request(
                                "/todos/${persistedEntities.first().id}",
                                HttpMethod.GET),
                        403
                ),
                row(
                        Request(
                                "/todos/",
                                HttpMethod.POST,
                                BaseTodoItemDto("title", true)),
                        403
                ),
                row(
                        Request(
                                "/todos/${persistedEntities.first().id}",
                                HttpMethod.PUT,
                                BaseTodoItemDto("title", true)),
                        403
                ),
                row(
                        Request(
                                "/todos/${persistedEntities.first().id}",
                                HttpMethod.DELETE),
                        403
                )
        ) { request, statusCode ->
            with(request) {
                restTemplate.exchange(
                        url,
                        httpMethod,
                        HttpEntity(body, headers),
                        Any::class.java)
            }.also(::println).statusCodeValue shouldBe statusCode
        }
    }

    private fun authHeader() = multiValueMapOf("x-api-key" to apiKey)

    private fun multiValueMapOf(vararg headers: Pair<String, String>) =
            headers.toMap()
                    .mapValues { (_, value) -> listOf(value) }
                    .let { map ->
                        HttpHeaders().apply { putAll(map) }
                    }
}