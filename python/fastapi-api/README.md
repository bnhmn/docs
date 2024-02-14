# ToDo List Service

With this service, you can manage your personal to-do list.
You can add new to-do entries, retrieve existing ones, update them, and delete them as needed.

This is a simple example of how to use [FastAPI](https://fastapi.tiangolo.com/) to build HTTP APIs with Python.

## How to Run

* Ensure Python 3 is installed on your system.
* Run `pip install -r requirements.txt` to install all Python libraries.
* Run `python -m uvicorn app:app --port 8000 --reload` to start the Service.
* Find the OpenAPI documentation of the running service here: http://127.0.0.1:8000/.

## How to Build

Execute `docker build -t todolist-service .` to build a Docker image.

Execute `docker run --rm -p 8000:8000 -it todolist-service` to run the Docker image.
