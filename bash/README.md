# Bash

[Bash](https://en.wikipedia.org/wiki/Bash_(Unix_shell)) is a command-line interpreter and scripting language used in
Unix and Linux systems, allowing users to execute commands and automate tasks.

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
