package com.instructure.bp.codelabs.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import org.springframework.stereotype.Component

@Component
class ApiKeyAuthenticationProvider : AuthenticationProvider {

    @Autowired
    private lateinit var apiKeyService: ApiKeyService

    override fun authenticate(authentication: Authentication): Authentication? {

        val token = authentication.credentials.toString()

        return if (apiKeyService.isApiKeyValid(token)) {
            PreAuthenticatedAuthenticationToken(null, null, listOf(SimpleGrantedAuthority("CAN_ACCESS_TODO"))).apply {
                isAuthenticated = true
            }
        } else {
            null
        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == PreAuthenticatedAuthenticationToken::class.java
    }
}