package todolist.api

import todolist.api.models.CreateTodoRequest
import todolist.api.models.TodoResponse
import todolist.api.models.UpdateTodoRequest
import todolist.model.Todo
import todolist.model.TodoCreate
import todolist.model.TodoState
import todolist.model.TodoUpdate

fun CreateTodoRequest.toModel(): TodoCreate {
    return TodoCreate(
        title = title,
        description = description,
        state = state?.toModel() ?: TodoState.OPEN,
    )
}

fun UpdateTodoRequest.toModel(): TodoUpdate {
    return TodoUpdate(title = title, description = description, state = state?.toModel())
}

fun todolist.api.models.TodoState.toModel(): TodoState {
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

fun TodoState.toResponse(): todolist.api.models.TodoState {
    return todolist.api.models.TodoState.valueOf(name.uppercase())
}
