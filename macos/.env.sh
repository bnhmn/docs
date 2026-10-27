#!/bin/zsh
# Include in .zshrc: source ~/.env.sh

# Global environment variables can be set in file /etc/profile
#export JAVA_HOME="/Library/Java/JavaVirtualMachines/openjdk-25.jdk/Contents/Home"
#export GRAALVM_HOME="/Library/Java/LibericaNativeImageKit/liberica-vm-25.0.2-openjdk25/Contents/Home"
#export PYTHON_HOME="/Library/Frameworks/Python.framework/Versions/3.15"
#export PYTHON_LIB="$HOME/Library/Python/3.15:$PATH"
#export GOPATH="$HOME/go"
# sudo ln -sf $HOME/.colima/default/docker.sock /var/run/docker.sock # required for colima
export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE="/var/run/docker.sock"  # required for colima
export DOCKER_HOST="unix://$HOME/.colima/default/docker.sock"        # required for colima
export PATH="$HOMEBREW_PREFIX/bin:$PATH"  # prefer homebrew installed binaries
#export PATH="$JAVA_HOME/bin:$PATH"
#export PATH="$PYTHON_HOME/bin:$PYTHON_LIB/bin:$PATH"
#export PATH="$HOME/scripts:$PATH"        # add custom scripts to PATH

alias ll="ls -lah"
alias python="python3"
alias pip="python3 -m pip"
alias globalpip="/usr/bin/python3 -m pip"
alias gc="git commit -am"
alias ga="git commit --amend --no-edit"
alias gaa="git commit -a --amend --no-edit"
alias gp="git push origin"
alias gpf="git push origin --force"
alias gs="git status"
alias reset="source ~/.zshrc"
alias ports="lsof -i -P | grep LISTEN"

# Git prune all branches that have been deleted from the remote
function gpr() {
  git checkout main 2>/dev/null || git checkout master 2>/dev/null || true
  git pull -p
  for branch in $(git for-each-ref --format '%(refname) %(upstream:track)' refs/heads | awk '$2 == "[gone]" {sub("refs/heads/", "", $1); print $1}')
  do
    git branch -D "$branch"
  done
}

# Auto active python venv on cd into directory
function python_venv() {
  MYVENV=./venv
  [[ -d $MYVENV ]] && source $MYVENV/bin/activate > /dev/null 2>&1      # when you cd into a folder that contains $MYVENV
  [[ ! -d $MYVENV ]] && deactivate > /dev/null 2>&1 || true             # when you cd into a folder that doesn't
}
autoload -U add-zsh-hook
add-zsh-hook chpwd python_venv
python_venv
