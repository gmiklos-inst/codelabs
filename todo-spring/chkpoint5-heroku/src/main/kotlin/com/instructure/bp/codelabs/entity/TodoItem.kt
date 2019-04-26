package com.instructure.bp.codelabs.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.instructure.bp.codelabs.dto.TodoItemDto
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class TodoItem(
        @Id
        @GeneratedValue(generator = "uuid")
        @GenericGenerator(name = "uuid", strategy = "uuid2")
        val id: String = "",

        val title: String,

        val completed: Boolean = false,

        @field:CreationTimestamp
        @Column(updatable = false)
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        val createdAt: OffsetDateTime? = null,

        @field:UpdateTimestamp
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        val updatedAt: OffsetDateTime? = null,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        val completedAt: OffsetDateTime? = null)

fun TodoItem.toDto(): TodoItemDto = TodoItemDto(
        id,
        title,
        completed,
        createdAt,
        updatedAt,
        completedAt
)