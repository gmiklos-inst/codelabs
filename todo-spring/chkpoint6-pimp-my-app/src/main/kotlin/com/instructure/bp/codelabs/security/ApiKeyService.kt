package com.instructure.bp.codelabs.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ApiKeyService {

    @Value("\${codelabs.security.apikey}")
    private lateinit var correctApiKey: String

    fun isApiKeyValid(apiKey: String) = (apiKey == correctApiKey)
}