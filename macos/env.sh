#!/bin/zsh

# Include in .zshrc and .bashrc: source ~/env.sh

# Global environment variables can be set in file /etc/profile
export JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home"
export PATH="/opt/homebrew/bin:$PATH"
export PATH="$JAVA_HOME/bin:$PATH"
export PATH="/Library/Frameworks/Python.framework/Versions/3.11/bin:/Users/Benjamin.Heimann/Library/Python/3.11/bin:$PATH"
export PATH="/Users/Benjamin.Heimann/scripts:$PATH"

alias "ll=ls -lah"
alias "python=python3"
alias "pip=python3 -m pip"
alias "globalpip=/usr/bin/python3 -m pip"
alias "kate=code"
alias "gc=git commit -am"
alias "ga=git commit --ammend --no-edit"
alias "gp=git push origin"
alias "gs=git status"
alias "reset=source ~/.zshrc"

# Auto active python venv on cd into directory
function python_venv() {
  MYVENV=./venv
  [[ -d $MYVENV ]] && source $MYVENV/bin/activate > /dev/null 2>&1      # when you cd into a folder that contains $MYVENV
  [[ ! -d $MYVENV ]] && deactivate > /dev/null 2>&1                     # when you cd into a folder that doesn't
}
autoload -U add-zsh-hook
add-zsh-hook chpwd python_venv
python_venv

# Replace in files
function replace() {
  if [ -z "${3-}" ]; then
    echo "ERROR: missing arguments. Usage: replace <file_path>... <search_regex> <replace_regex>"
    return 1
  fi
  args=("${@}")
  sed -i "" -E s/"${args[-2]}"/"${args[-1]}/"g "${args[@]:0:-2}"
}
