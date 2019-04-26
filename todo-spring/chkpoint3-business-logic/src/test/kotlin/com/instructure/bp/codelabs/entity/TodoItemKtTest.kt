package com.instructure.bp.codelabs.entity

import com.instructure.bp.codelabs.dto.TodoItemDto
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

class TodoItemKtTest {

    @Test
    fun `toDto converts entity to dto`() {
        val createdAt = OffsetDateTime.now().minusDays(2)
        val updatedAt = OffsetDateTime.now().minusDays(1)
        val completedAt = OffsetDateTime.now()

        val expectedDto = TodoItemDto(
                "id",
                "title",
                true,
                createdAt,
                updatedAt,
                completedAt)
        val inputEntity = TodoItem(
                "id",
                "title",
                true,
                createdAt,
                updatedAt,
                completedAt)

        inputEntity.toDto() shouldBe expectedDto
    }
}