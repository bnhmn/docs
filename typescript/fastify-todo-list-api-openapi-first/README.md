# ToDo List API

With this service, you can manage your personal to-do list.
You can add new to-do entries, retrieve existing ones, update them, and delete them as needed.

This is a simple example of how to use [fastify](https://fastify.dev/) to build HTTP APIs with TypeScript and Node.js.

## How to Run

- Ensure [Node.js](https://nodejs.org/en) is installed on your system.
- Run `npm ci` to install the dependencies.
- Run `npm run codegen` to generate the server stubs from the OpenAPI spec.
- Run `npm run dev` to start the server: <http://localhost:8000/>.

## How to Contribute

### Code Style

This project uses [ESlint](https://eslint.org/) and [Prettier](https://prettier.io/) to ensure code quality.

- Run `npm run check` to verify that your code complies with the coding rules.
- Run `npm run format` to format your code.

We recommend to use [VS Code](https://code.visualstudio.com/) as IDE in combination with the
[ESLint](https://marketplace.visualstudio.com/items?itemName=dbaeumer.vscode-eslint) and
[Prettier](https://marketplace.visualstudio.com/items?itemName=esbenp.prettier-vscode) extensions,
because it shows you any error messages directly in the code and can automatically format your code upon saving.
