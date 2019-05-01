package com.instructure.bp.codelabs.controller

import com.instructure.bp.codelabs.dto.ErrorDto
import com.instructure.bp.codelabs.dto.ErrorResponseDto
import io.kotlintest.data.forall
import io.kotlintest.matchers.types.shouldBeSameInstanceAs
import io.kotlintest.shouldBe
import io.kotlintest.tables.row
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.springframework.http.HttpInputMessage
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.web.context.request.WebRequest
import javax.persistence.EntityNotFoundException

internal class ControllerExceptionHandlerTest {

    private class TestException : Exception()

    private lateinit var controllerExceptionHandler: ControllerExceptionHandler

    @BeforeEach
    fun init() {
        controllerExceptionHandler = ControllerExceptionHandler()
    }

    @Test
    fun `exceptions are mapped to correct response body`() {
        forall(
                row(
                        HttpMessageNotReadableException("", mock(HttpInputMessage::class.java)),
                        400,
                        ErrorResponseDto(listOf(ErrorDto(0, "Incorrect message")))),
                row(
                        EntityNotFoundException(),
                        404,
                        ErrorResponseDto(listOf(ErrorDto(1, "Entity not found")))),
                row(
                        JpaObjectRetrievalFailureException(EntityNotFoundException()),
                        404,
                        ErrorResponseDto(listOf(ErrorDto(1, "Entity not found"))))
        ) { exception, expectedStatusCode, expectedResponseBody ->
            controllerExceptionHandler.handleException(exception, mock(WebRequest::class.java)).run {
                statusCodeValue shouldBe expectedStatusCode
                body shouldBe expectedResponseBody
            }
        }
    }

    @Test
    fun `throws unknown exception`() {
        TestException().let { unknownException ->
            assertThrows<TestException> {
                controllerExceptionHandler.handleException(unknownException, mock(WebRequest::class.java))
            } shouldBeSameInstanceAs unknownException
        }
    }
}