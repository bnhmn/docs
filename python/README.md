# Python

[Python](https://www.python.org/) is a high-level, interpreted programming language known for its clear syntax
and readability. It supports multiple programming paradigms, including procedural, object-oriented, and functional
programming. Python is widely used for web development, data analysis, artificial intelligence, scientific computing,
automation, and more, making it a versatile tool favored by both beginners and experienced developers.

## Python Tooling

* [ruff](https://github.com/astral-sh/ruff) - A fast Python linter and code formatter, written in Rust.
* [uv](https://github.com/astral-sh/uv) - A fast Python package and project manager, written in Rust.
* [pyproject.toml](https://packaging.python.org/en/latest/guides/writing-pyproject-toml/) - the new unified Python
  configuration file. It replaces both setup.py and requirements.txt.

## Show Python installation path

```bash
python -m site
```

## Show Platform Infos

```bash
python -c "import platform; print(platform.system(), platform.machine())"
```

```bash
Darwin arm64
```
