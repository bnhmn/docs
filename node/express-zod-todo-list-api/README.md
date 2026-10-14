# ToDo List API

With this service, you can manage your personal to-do list.
You can add new to-do entries, retrieve existing ones, update them, and delete them as needed.

This is a simple example of how to use [Express.js](https://expressjs.com/en/guide/routing.html) to build HTTP APIs with
TypeScript and Node.js.

## How to Run

- Ensure [Node.js](https://nodejs.org/en) is installed on your system.
- Run `npm ci` to install the dependencies.
- Run `npm run dev` to start the server: <http://localhost:8000/>.

## Run in CI

1. Run `npm ci` to download the project dependencies.
2. Run `npm run build` to build the project.
3. Run `docker build -t todo-list-api .` to build a Docker image.
4. Run `docker run --rm -p 8000:8000 --init todo-list-api` to execute the Docker image.
