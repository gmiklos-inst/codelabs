package com.instructure.bp.codelabs.dto

import com.instructure.bp.codelabs.entity.TodoItem
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

class TodoItemDtoKtTest {

    @Test
    fun `toEntity converts dto to entity`() {
        val createdAt = OffsetDateTime.now().minusDays(2)
        val updatedAt = OffsetDateTime.now().minusDays(1)
        val completedAt = OffsetDateTime.now()

        val expectedEntity = TodoItem(
                "id",
                "title",
                true,
                createdAt,
                updatedAt,
                completedAt)
        val inputDto = TodoItemDto(
                "id",
                "title",
                true,
                createdAt,
                updatedAt,
                completedAt)

        inputDto.toEntity() shouldBe expectedEntity
    }
}