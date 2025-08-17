# macOS

This folder contains information for macOS.

## Useful Shortcuts

See [shortcuts.md](shortcuts.md).

## Useful Applications

### Via Google

* Homebrew
* Oh my ZSH
* VS Code
* IntelliJ IDEA
* [serviceman](https://webinstall.dev/serviceman/) (systemd, launchd manager)
* Karabiner-Elements (Keyboard customization): <https://karabiner-elements.pqrs.org/>
* Rectangle (Snap windows to the left, right, etc.)
* KeepingYouAwake
* [Swift Quit](https://swiftquit.com/) (Close Mac Applications Automatically When Their Windows Close)

### Via Homebrew

* openjdk@17
* maven
* node
* fzf (fuzzy finder)
* htop
* gettext
* watch
* colima (Docker daemon)
* ccat (colorized cat)
* sevenzip
* z (warp into directory)
* romkatv/powerlevel10k/powerlevel10k (ZSH theme)

### Scripts

* [env.sh](env.sh) - Shell startup script
* [toggle-screen-resolution.sh](toggle-screen-resolution.sh) - Shell script assigned to a custom Keyboard Shortcut
* Open Terminal here: <https://github.com/jbtule/cdto#cd-to>
* Right click -> New File: <https://apps.apple.com/in/app/new-file-menu-free/id1066302071?mt=12>
* display_manager.py: <https://github.com/univ-of-utah-marriott-library-apple/display_manager>
  cp /Library/Python/2.7/site-packages/display_manager_lib.py /Library/Frameworks/Python.framework/Versions/3.11/lib/python3.11/site-packages/ && pip install pyobjc

## Build and Run Docker Containers

Use [colima](https://github.com/abiosoft/colima).
Works with docker compose and buildx too: <https://aosolorzano.medium.com/1ce8b3bae158>

## Run Ubuntu VM

Use [multipass](https://multipass.run/install).

```bash
multipass list
```
