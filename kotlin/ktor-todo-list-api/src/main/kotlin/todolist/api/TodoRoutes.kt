package todolist.api

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.util.getOrFail
import todolist.model.TodoService

// TODO: DI
val service = TodoService()

fun Route.todoRoutes() {
    route("/todos") {
        post {
            val request = call.receive<CreateTodoRequest>()
            // TODO: validation
            val newTodo = service.createTodo(request.toModel())
            call.response.header(HttpHeaders.Location, "/todos/${newTodo.id}")
            call.response.status(HttpStatusCode.Created)
            call.respond(newTodo.toResponse())
        }
        get() {
            val todos = service.fetchTodos()
            call.respond(todos.toResponse())
        }
        get("/{todoId}") {
            val todoId = call.parameters.getOrFail<Int>("todoId")
            val todo = service.fetchTodoById(todoId)
            if (todo != null) {
                call.respond(todo.toResponse())
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        patch("/{todoId}") {
            val todoId = call.parameters.getOrFail<Int>("todoId")
            val update = call.receive<UpdateTodoRequest>()
            val updatedTodo = service.updateTodoById(todoId, update.toModel())
            if (updatedTodo != null) {
                call.respond(updatedTodo.toResponse())
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        delete("/{todoId}") {
            val todoId = call.parameters.getOrFail<Int>("todoId")
            val deleted = service.deleteTodoById(todoId)
            if (deleted) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
