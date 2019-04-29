package com.instructure.bp.codelabs.configuration

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Pageable
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.time.Clock
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer

@Configuration
class WebConfiguration {

    @Bean
    fun jacksonBuilder() = Jackson2ObjectMapperBuilder().apply {
        propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
        serializationInclusion(JsonInclude.Include.NON_NULL)
        featuresToDisable(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        )
    }

    @Bean
    fun clock() = Clock.systemDefaultZone()

    @Bean
    fun pageableResolverCustomizer() = PageableHandlerMethodArgumentResolverCustomizer { pageableResolver ->
        pageableResolver.setFallbackPageable(Pageable.unpaged())
    }
}