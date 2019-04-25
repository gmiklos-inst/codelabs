package com.instructure.bp.codelabs.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotlintest.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset

class WebConfigurationTest {

    data class DummyUser(val firstName: String, val age: Int? = null)

    lateinit var mapper: ObjectMapper

    @BeforeEach
    fun init() {
        mapper = WebConfiguration().jacksonBuilder().build()
    }

    @Test
    fun `jacksonBuilder's objectMapper should use snake_case for serialization`() {
        val actualJson = mapper.writeValueAsString(DummyUser("Justin", 43))
        val expectedJson = """{"first_name":"Justin","age":43}"""

        actualJson shouldBe expectedJson
    }

    @Test
    fun `jacksonBuilder's objectMapper should ignore null fields for serialization`() {
        val actualJson = mapper.writeValueAsString(DummyUser("Justin"))
        val expectedJson = """{"first_name":"Justin"}"""

        actualJson shouldBe expectedJson
    }

    @Test
    fun `jacksonBuilder's objectMapper should serialize OffsetDateTime as String`() {
        val expectedFormatting = "\"1876-10-05T13:12:05.001-02:00\""
        val dateTime = OffsetDateTime.of(1876, 10, 5, 13, 12, 5, 1_000_000, ZoneOffset.ofHours(-2))
        val actualFormatting = mapper.writeValueAsString(dateTime)

        actualFormatting shouldBe expectedFormatting
    }

}