package com.instructure.bp.codelabs.controller

import com.instructure.bp.codelabs.dto.ErrorDto
import com.instructure.bp.codelabs.dto.ErrorResponseDto
import org.springframework.dao.DataAccessException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.http.HttpStatus
import org.springframework.web.context.request.WebRequest
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.persistence.EntityNotFoundException

@ControllerAdvice
class ControllerExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponseDto> =
            when (ex) {
                is HttpMessageNotReadableException -> responseEntity(HttpStatus.BAD_REQUEST, 0, "Incorrect message")

                is EntityNotFoundException,
                is DataAccessException -> responseEntity(HttpStatus.NOT_FOUND, 1, "Entity not found")

                else -> throw ex
            }

    private fun responseEntity(status: HttpStatus, errorCode: Int, errorMessage: String) =
            ResponseEntity
                    .status(status)
                    .body(ErrorResponseDto(listOf(
                            ErrorDto(errorCode, errorMessage)
                    )))
}