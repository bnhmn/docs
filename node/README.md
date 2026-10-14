# Node.js and TypeScript

[Node.js](https://nodejs.org/) is a
[JavaScript](https://developer.mozilla.org/de/docs/Web/JavaScript) runtime that lets you run
JavaScript outside the browser. It is commonly used to build servers, command-line tools, and
development tooling.

[TypeScript](https://www.typescriptlang.org/) adds static types to JavaScript. Its type checker can
catch many mistakes during development, making code easier to maintain, especially in larger
projects. TypeScript is typically compiled to JavaScript, which can then run in Node.js or in the
browser.

## TypeScript Tooling

- [biome](https://github.com/biomejs/biome) - A fast code formatter and linter, written in Rust.
- [lefthook](https://lefthook.dev/intro.html) - A Git hooks manager.

## Debug npm config

The [npm configuration](https://docs.npmjs.com/cli/v8/using-npm/config) file is usually located at
`~/.npmrc`.

```bash
npm config list
```

## Print all npm dependencies

```bash
npm list --depth=3 >> result.txt
```
