package todolist.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import todolist.model.Todo
import todolist.model.TodoCreate
import todolist.model.TodoState
import todolist.model.TodoUpdate

@Serializable
data class CreateTodoRequest(
    val title: String,
    val description: String,
    val state: RestTodoState = RestTodoState.Open,
)

@Serializable
data class UpdateTodoRequest(
    val title: String? = null,
    val description: String? = null,
    val state: RestTodoState? = null,
)

@Serializable
data class TodoResponse(
    val id: Int,
    var title: String,
    var description: String,
    var state: RestTodoState,
)

enum class RestTodoState {
    @SerialName("open")
    Open,
    @SerialName("done")
    Done,
}

fun CreateTodoRequest.toModel(): TodoCreate {
    return TodoCreate(title = title, description = description, state = state.toModel())
}

fun UpdateTodoRequest.toModel(): TodoUpdate {
    return TodoUpdate(title = title, description = description, state = state?.toModel())
}

fun RestTodoState.toModel(): TodoState {
    return TodoState.valueOf(name)
}

fun Todo.toResponse(): TodoResponse {
    return TodoResponse(
        id = id,
        title = title,
        description = description,
        state = state.toResponse(),
    )
}

fun List<Todo>.toResponse(): List<TodoResponse> {
    return this.map { it.toResponse() }
}

fun TodoState.toResponse(): RestTodoState {
    return RestTodoState.valueOf(name)
}
