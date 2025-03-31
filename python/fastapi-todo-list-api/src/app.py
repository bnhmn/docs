from textwrap import dedent

import uvicorn
from fastapi import FastAPI
from starlette.responses import RedirectResponse, JSONResponse

import todos
from errors import ApiError
from model import HealthResponse, TodoState, CreateTodoRequest, TodoResponse, UpdateTodoRequest

app = FastAPI(
    title="ToDo List Service",
    description=dedent(
        """
        With this service, you can manage your personal to-do list. 
        You can add new to-do entries, retrieve existing ones, 
        update them, and delete them as needed.
        """
    ),
)


@app.post("/todos")
def create_todo(todo: CreateTodoRequest) -> TodoResponse:
    todo = todos.create_todo(todo)
    return todo.to_response()


@app.get("/todos")
def get_todos() -> list[TodoResponse]:
    todo_list = todos.get_todos()
    ret = [todo.to_response() for todo in todo_list]
    return ret


@app.delete("/todos", description="Delete all todos or todos with a certain state")
def delete_todos(state: TodoState = None) -> list[TodoResponse]:
    new_todo_list = todos.delete_todos(state)
    return [todo.to_response() for todo in new_todo_list]


@app.get("/todos/{todo_id}")
def get_todo(todo_id: int) -> TodoResponse:
    todo = todos.get_todo(todo_id)
    return todo.to_response()


@app.patch("/todos/{todo_id}")
def update_todo(todo_id: int, update: UpdateTodoRequest) -> TodoResponse:
    todo = todos.update_todo(todo_id, update)
    return todo.to_response()


@app.delete("/todos/{todo_id}", status_code=204)
def delete_todo(todo_id: int):
    return todos.delete_todo(todo_id)


@app.get("/", include_in_schema=False)
def index():
    return RedirectResponse("/redoc")


@app.get("/health", include_in_schema=False)
def health() -> HealthResponse:
    return HealthResponse()


@app.exception_handler(ApiError)
def exception_handler(_, error: ApiError):
    return JSONResponse(
        status_code=error.status,
        content={"status": error.status, "code": error.code, "message": error.message},
    )


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
