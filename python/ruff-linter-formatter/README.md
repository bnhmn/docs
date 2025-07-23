# Ruff

This project uses [Ruff](https://github.com/astral-sh/ruff) and [pre-commit](https://pre-commit.com/) to ensure code quality.

* Ensure [Python 3](https://www.python.org/downloads/) is installed on your system.
* Install the dependencies using `pip install -r requirements-dev.txt`.
* Run `ruff format && ruff check --fix` to format and lint your code.
* Run `pre-commit install` to enable the pre-commit hook that ensures that no violations are commited.
