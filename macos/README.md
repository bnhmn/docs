# MacOS 

This folder contains information and scripts for Mac Operating System.

## Useful Shortcuts

```plain
Windows-Taste=Command ⌘
Alt-Taste=Option ⌥
Strg-Taste ⌃

Screenshot:                                   Shift-Cmd-4
Refresh:                                      Cmd-R
Mac sperren:                                  Strg-Cmd-Q
Teams muten:                                  Cmd-Shift-M
Bildschirmauflösung umschalten:               Strg-Alt-M
Spotlight (App öffnen):                       Cmd-Space

Finder Versteckte Dateien anzeigen:           Cmd-Shift-.
Finder Go To Folder:                          Cmd-Shift-G
Finder Neuer Ordner:                          Cmd-Shift-N
Finder Pfad kopieren:                         Rechtsklick auf Datei + Alt-Taste drücken

VS Code Command Palette:                      Cmd-Shift-P
VS Code Multicursor bei Auswahl einfügen:     Alt-Shift-I
VS Code Find in Folder:                       Alt-Shift-F
VS Code Diff:                                 Palette -> Compare 2 folders
VS Code Search & Replace:                     Cmd-F

Eclipse Show Javadoc:                         F2
Eclipse Format Source Code (ex. Imports):     Cmd-Shift-F
Eclipse Organize Imports:                     Cmd-Shift-O
Eclipse Find in Files:                        Strg-H
Eclipse Toggle Comment:                       Cmd-7
Eclipse neue Instanz starten:                 open -na eclipse
Eclipse Terminal öffnen:                      Strg-Alt-T

Unsignierte DMG Apps installieren:            xattr -d com.apple.quarantine <NAME>.pkg
Python Pfade anzeigen:                        python -m site
Strg+Space default:                           switch input sources 
                                              (disable under: Preferences>Keyboard>Shortcuts>Input Sources)
```

## Files

* [env.sh](env.sh) - Shell startup script

## Useful Applications

### Google
- Homebrew
- Oh my ZSH
- VS Code
- IntelliJ IDEA
- serviceman (systemd, launchd manager)
- Karabiner-Elements (Keyboard customization): https://karabiner-elements.pqrs.org/
- Rectangle (Snap windows to the left, right, etc.)
- KeepingYouAwake

### Homebrew
- openjdk@17
- maven
- fzf (fuzzy finder)
- htop
- gettext
- watch
- colima (Docker daemon)
- ccat (colorized cat)
- sevenzip
- z (warp into directory) 
- romkatv/powerlevel10k/powerlevel10k (ZSH theme)

### Scripts
- Open Terminal here: https://github.com/jbtule/cdto#cd-to
- Right click -> New File: https://apps.apple.com/in/app/new-file-menu-free/id1066302071?mt=12
- display_manager.py: https://github.com/univ-of-utah-marriott-library-apple/display_manager
  cp /Library/Python/2.7/site-packages/display_manager_lib.py /Library/Frameworks/Python.framework/Versions/3.11/lib/python3.11/site-packages/ && pip install pyobjc
