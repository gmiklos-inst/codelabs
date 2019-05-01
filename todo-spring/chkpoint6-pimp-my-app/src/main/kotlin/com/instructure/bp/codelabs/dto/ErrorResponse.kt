package com.instructure.bp.codelabs.dto

data class ErrorResponseDto(val errors: List<ErrorDto>)

data class ErrorDto(val code: Int, val title: String)