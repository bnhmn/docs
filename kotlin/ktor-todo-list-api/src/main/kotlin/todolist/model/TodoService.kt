package todolist.model

class TodoService {
    // TODO: DI
    private val repository: TodoRepository = InMemoryTodoRepository()

    fun createTodo(request: TodoCreate): Todo {
        return repository.createTodo(request)
    }

    fun fetchTodos(): List<Todo> {
        return repository.findAllTodos()
    }

    fun fetchTodoById(id: Int): Todo? {
        return repository.findTodoById(id)
    }

    fun updateTodoById(id: Int, request: TodoUpdate): Todo? {
        return repository.updateTodoById(id, request)
    }

    fun deleteTodoById(id: Int): Boolean {
        return repository.deleteTodoById(id)
    }
}
