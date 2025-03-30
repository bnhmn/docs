package todolist.model

data class Todo(val id: Int, var title: String, var description: String, var state: TodoState)

data class TodoCreate(val title: String, val description: String, val state: TodoState)

data class TodoUpdate(val title: String?, val description: String?, val state: TodoState?)

enum class TodoState {
    OPEN,
    DONE,
}
