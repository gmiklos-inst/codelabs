package com.instructure.bp.codelabs.configuration

import com.instructure.bp.codelabs.security.ApiKeyAuthenticationProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.AuthenticationManager





@Configuration
@EnableWebSecurity
class SecurityConfiguration: WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var apiKeyAuthenticationProvider: ApiKeyAuthenticationProvider

    override fun configure(http: HttpSecurity) {
        http.antMatcher("/**")
                .addFilter(preAuthenticationFilter())
                .authorizeRequests().anyRequest().hasAuthority("CAN_ACCESS_TODO")
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
    }

    override fun authenticationManager(): AuthenticationManager {
        return ProviderManager(listOf(apiKeyAuthenticationProvider))
    }

    @Bean
    fun preAuthenticationFilter() = RequestHeaderAuthenticationFilter().apply {
        setPrincipalRequestHeader("x-api-key")
        setCredentialsRequestHeader("x-api-key")
        setAuthenticationManager(authenticationManager())
        setExceptionIfHeaderMissing(false)
    }
}