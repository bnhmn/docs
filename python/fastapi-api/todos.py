from errors import TodoNotFound
from model import CreateTodoRequest, UpdateTodoRequest, Todo, TodoState

todos: dict[int, Todo] = {
    1: Todo(id=1, title="Buy flowers"),
    2: Todo(id=2, title="Cook dinner"),
    3: Todo(id=3, title="Clean the bathroom", state=TodoState.done)
}
next_todo_id = len(todos) + 1


def create_todo(todo: CreateTodoRequest) -> Todo:
    global next_todo_id

    todo = Todo(
        id=next_todo_id,
        title=todo.title,
        description=todo.description,
        state=todo.state
    )
    todos[todo.id] = todo

    next_todo_id += 1

    return todo


def get_todos() -> list[Todo]:
    return list(todos.values())


def get_todo(todo_id: int) -> Todo:
    check_todo_exists(todo_id)
    return todos[todo_id]


def update_todo(todo_id: int, update: UpdateTodoRequest) -> Todo:
    check_todo_exists(todo_id)
    todo = todos[todo_id]

    if update.title:
        todo.title = update.title
    if update.description:
        todo.description = update.description
    if update.state:
        todo.state = update.state

    return todo


def delete_todo(todo_id: int):
    check_todo_exists(todo_id)
    todos.pop(todo_id)


def delete_todos(state: TodoState = None) -> list[Todo]:
    if not state:
        todos.clear()
    else:
        new_todos = {todo_id: todo for todo_id, todo in todos.items() if todo.state != state}
        todos.clear()
        todos.update(new_todos)

    return get_todos()


def check_todo_exists(todo_id):
    if todo_id not in todos:
        raise TodoNotFound(message=f"ToDo entry '{todo_id}' could not be found")
