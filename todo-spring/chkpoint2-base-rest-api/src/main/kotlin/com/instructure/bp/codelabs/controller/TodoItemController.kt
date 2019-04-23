package com.instructure.bp.codelabs.controller

import com.instructure.bp.codelabs.dto.TodoItemDto
import com.instructure.bp.codelabs.service.TodoItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/todos")
class TodoItemController {

    @Autowired
    private lateinit var todoItemService: TodoItemService

    @GetMapping
    fun getTodoItems(): List<TodoItemDto> = todoItemService.getAllTodoItems()

    @PostMapping
    fun addTodoItem(@RequestBody todoItemDto: TodoItemDto): ResponseEntity<TodoItemDto> {
        val createdTodoItem = todoItemService.createTodoItem(todoItemDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodoItem)
    }

    @PutMapping("/{id}")
    fun updateTodoItem(
            @PathVariable("id") id: String,
            @RequestBody todoItemDto: TodoItemDto): TodoItemDto = todoItemService.updateTodoItem(id, todoItemDto)

    @DeleteMapping("/{id}")
    fun deleteTodoItem(@PathVariable("id") id: String) {
        todoItemService.deleteTodoItem(id)
    }
}