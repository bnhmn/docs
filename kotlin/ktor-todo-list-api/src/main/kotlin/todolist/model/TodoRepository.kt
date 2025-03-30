package todolist.model

import todolist.model.TodoState.Done
import todolist.model.TodoState.Open

interface TodoRepository {
    fun createTodo(request: TodoCreate): Todo

    fun findAllTodos(): List<Todo>

    fun findTodoById(id: Int): Todo?

    fun updateTodoById(id: Int, request: TodoUpdate): Todo?

    fun deleteTodoById(id: Int): Boolean
}

class InMemoryTodoRepository : TodoRepository {
    private val todos =
        mutableListOf(
            Todo(id = 1, title = "Buy flowers", description = "Buy some flowers", state = Open),
            Todo(id = 2, title = "Cook dinner", description = "Cook some dinner", state = Open),
            Todo(id = 3, title = "Clean bathroom", description = "Clean the bathroom", state = Done),
        )
    private var nextId = todos.size + 1

    override fun createTodo(request: TodoCreate): Todo {
        val todo =
            Todo(
                id = nextId++,
                title = request.title,
                description = request.description,
                state = request.state,
            )
        todos.add(todo)
        return todo
    }

    override fun findAllTodos(): List<Todo> {
        return todos
    }

    override fun findTodoById(id: Int): Todo? {
        return todos.find { it.id == id }
    }

    override fun updateTodoById(id: Int, request: TodoUpdate): Todo? {
        val todo = findTodoById(id)
        if (todo != null) {
            request.title?.let { todo.title = it }
            request.description?.let { todo.description = it }
            request.state?.let { todo.state = it }
        }
        return todo
    }

    override fun deleteTodoById(id: Int): Boolean {
        val todoIndex = todos.indexOfFirst { it.id == id }
        if (todoIndex >= 0) {
            todos.removeAt(todoIndex)
            return true
        } else {
            return false
        }
    }
}
