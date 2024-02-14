from dataclasses import dataclass
from enum import Enum

from pydantic import BaseModel, Field

TODO_TITLE_EXAMPLES = ["Buy milk", "Do the laundry", "Clean up the kitchen"]
TODO_DESCR_EXAMPLES = ["Bring some milk from the supermarket!"]


class HealthResponse(BaseModel):
    status: str = "UP"


class TodoState(str, Enum):
    open = "open"
    done = "done"


class CreateTodoRequest(BaseModel):
    title: str = Field(min_length=1, max_length=100, examples=TODO_TITLE_EXAMPLES)
    description: str = Field(min_length=0, max_length=500, examples=TODO_DESCR_EXAMPLES, default="")
    state: TodoState = Field(default=TodoState.open)


class UpdateTodoRequest(BaseModel):
    title: str = Field(min_length=1, max_length=100, examples=TODO_TITLE_EXAMPLES, default=None)
    description: str = Field(min_length=0, max_length=500, examples=TODO_DESCR_EXAMPLES, default=None)
    state: TodoState = Field(default=None)


class TodoResponse(BaseModel):
    id: int = Field(examples=[1])
    title: str = Field(examples=TODO_TITLE_EXAMPLES)
    description: str = Field(examples=TODO_DESCR_EXAMPLES)
    state: TodoState


@dataclass
class Todo:
    id: int
    title: str
    description: str = ""
    state: TodoState = TodoState.open

    def to_response(self) -> TodoResponse:
        return TodoResponse(
            id=self.id,
            title=self.title,
            description=self.description,
            state=self.state,
        )
