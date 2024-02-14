from dataclasses import dataclass


@dataclass
class ApiError(RuntimeError):
    message: str
    status: int
    code: str


@dataclass
class TodoNotFound(ApiError):
    message: str
    status: int = 404
    code: str = "TODO_NOT_FOUND"
