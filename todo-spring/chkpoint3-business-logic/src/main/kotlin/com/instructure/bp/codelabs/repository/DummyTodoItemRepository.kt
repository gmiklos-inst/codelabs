package com.instructure.bp.codelabs.repository

import com.instructure.bp.codelabs.entity.TodoItem
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime

@Repository
class DummyTodoItemRepository : TodoItemRepository {
    private companion object {
        val TODO_ITEMS = listOf(
                TodoItem("1", "title1", true, OffsetDateTime.now()),
                TodoItem("2", "title2", true, OffsetDateTime.now()),
                TodoItem("3", "title3", false, OffsetDateTime.now()),
                TodoItem("4", "title4", true, OffsetDateTime.now())
        )
        val TODO_ITEM = TODO_ITEMS.first()
        val ID = TODO_ITEM.id
    }
    override fun findAll() = TODO_ITEMS

    override fun getOne(id: String) = TODO_ITEM

    override fun save(todoItem: TodoItem) = TODO_ITEM

    override fun exists(id: String)=true

    override fun deleteById(id: String) {

    }
}
