package com.instructure.bp.codelabs.controller

import com.instructure.bp.codelabs.dto.BaseTodoItemDto
import com.instructure.bp.codelabs.dto.TodoItemDto
import com.instructure.bp.codelabs.service.TodoItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/todos")
class TodoItemController {

    private companion object {
        const val TOTAL_COUNT = "X-Total-Count"
    }

    @Autowired
    private lateinit var todoItemService: TodoItemService

    @GetMapping
    fun getTodoItems(pageable: Pageable): ResponseEntity<List<TodoItemDto>> {
        val page = todoItemService.getAllTodoItems(pageable)
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(TOTAL_COUNT, "${page.totalElements}")
                .body(page.toList())
    }

    @GetMapping("/{id}")
    fun getTodoItem(@PathVariable("id") id: String): TodoItemDto {
        return todoItemService.getTodoItem(id)
    }

    @PostMapping
    fun addTodoItem(@RequestBody baseTodoItemDto: BaseTodoItemDto): ResponseEntity<TodoItemDto> {
        val createdTodoItem = todoItemService.createTodoItem(baseTodoItemDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodoItem)
    }

    @PutMapping("/{id}")
    fun updateTodoItem(
            @PathVariable("id") id: String,
            @RequestBody baseTodoItemDto: BaseTodoItemDto): TodoItemDto {
        return todoItemService.updateTodoItem(id, baseTodoItemDto)
    }

    @DeleteMapping("/{id}")
    fun deleteTodoItem(@PathVariable("id") id: String): ResponseEntity<Nothing> {
        todoItemService.deleteTodoItem(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
    }
}