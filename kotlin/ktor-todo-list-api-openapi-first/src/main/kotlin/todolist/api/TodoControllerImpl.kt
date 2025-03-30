package todolist.api

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.header
import io.ktor.server.response.respond
import todolist.api.controllers.TodosController
import todolist.api.controllers.TypedApplicationCall
import todolist.api.models.CreateTodoRequest
import todolist.api.models.TodoResponse
import todolist.api.models.UpdateTodoRequest
import todolist.model.TodoService
import todolist.validate

class TodoControllerImpl(private val service: TodoService) : TodosController {
    override suspend fun createTodo(
        createTodoRequest: CreateTodoRequest,
        call: TypedApplicationCall<TodoResponse>,
    ) {
        createTodoRequest.validate()
        val newTodo = service.createTodo(createTodoRequest.toModel())
        call.response.header(HttpHeaders.Location, "/todos/${newTodo.id}")
        call.response.status(HttpStatusCode.Created)
        call.respondTyped(newTodo.toResponse())
    }

    override suspend fun fetchTodos(call: TypedApplicationCall<List<TodoResponse>>) {
        val todos = service.fetchTodos()
        call.respondTyped(todos.toResponse())
    }

    override suspend fun fetchTodoById(id: Int, call: TypedApplicationCall<TodoResponse>) {
        val todo = service.fetchTodoById(id)
        if (todo != null) {
            call.respondTyped(todo.toResponse())
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    override suspend fun updateTodoById(
        id: Int,
        updateTodoRequest: UpdateTodoRequest,
        call: TypedApplicationCall<TodoResponse>,
    ) {
        updateTodoRequest.validate()
        val updatedTodo = service.updateTodoById(id, updateTodoRequest.toModel())
        if (updatedTodo != null) {
            call.respondTyped(updatedTodo.toResponse())
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    override suspend fun deleteTodoById(id: Int, call: ApplicationCall) {
        val deleted = service.deleteTodoById(id)
        if (deleted) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}
