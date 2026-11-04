# Bash

[Bash](<https://en.wikipedia.org/wiki/Bash_(Unix_shell)>) is a command-line interpreter and
scripting language used in Unix and Linux systems, allowing users to execute commands and automate
tasks.

## Bash Cheatsheet

See <https://devhints.io/bash>.

## Env Variables

The snippet below shows how to declare required environment variables and optional environment
variables with default values. Values can be provided or overridden by the calling environment:

```bash
#!/usr/bin/env bash
set -euo pipefail

: "${DATABASE_USER:=admin}"
: "${DATABASE_PASSWORD:?missing env variable}"
```

## Bash Options

Bash options are settings that modify the shell's behavior. They are set using the set command.
Common options like these enhance error handling and debugging in scripts:

- `-e` or `-o errexit`: Exit on command failure.
- `-u` or `-o nounset`: Treat unset variables as errors.
- `-x` or `-o xtrace`: Show each command before execution.
- `-o pipefail`: Makes a pipeline return the status of the last command that failed.

The website [explainshell.com](https://explainshell.com/explain?cmd=set+-eu) helps you understand
bash options.

## Read files relative to script dir

```bash
#!/bin/bash
set -o errexit -o nounset -o pipefail

script_dir="$(realpath "$(dirname "${BASH_SOURCE[0]}")")"

for dir in "$script_dir"/[!.]*/; do
  echo "Reading '$dir'"
done
```

## Run command as root but keep home directory of the invoking user

```bash
sudo -H <the-command>
```
