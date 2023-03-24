# MacOS

This folder contains information and scripts for Mac Operating System.

## Useful Shortcuts

```plain
Windows-Key=Command ⌘
Alt-Key=Option ⌥
Ctrl-Key ⌃

Screenshot:                                   Shift-Cmd-4
Refresh:                                      Cmd-R
Mac Lock screen:                              Ctrl-Cmd-Q
Teams Mute:                                   Cmd-Shift-M
Switch screen resolution (custom):            Ctrl-Alt-M
Spotlight (search Apps):                      Cmd-Space
Browser Open Developer Console:               F12

Finder Show hidden files:                     Cmd-Shift-.
Finder Go To Folder:                          Cmd-Shift-G
Finder New Folder:                            Cmd-Shift-N
Finder Copy Path:                             Right Click File + Press Alt-Key

Intellij Search Everywhere (Command-Palette): Shift Shift
Intellij Rename:                              Shift-F6
Intellij Find Usages:                         Alt-F7
Intellij Add another Cursor at position:      Alt-Shift-Click
Intellij Add Cursor above or below:           Alt Alt-UP/DOWN
Intellij Add Cursors in selection:            Alt-Shift-G
Intellij Open Settings:                       Cmd-,
Intellij Toggle Line Comment:                 Cmd-NUMPADDIVIDE
Intellij Toggle Block Comment:                Cmd-Alt-NUMPADDIVIDE
Intellij Debug:                               Shift-F9
Intellij Execute File:                        Alt-Shift-F10
Intellij Move Code Fragment:                  Alt-Shift-UP/DOWN

VS Code Command-Palette:                      Cmd-Shift-P
VS Code Reformat Code:                        Alt-Shift-F
VS Code Search & Replace:                     Cmd-F
VS Code Find in Folder:                       Alt-Shift-F
VS Code Compare Folders (Plugin):             Command-Palette -> Compare 2 folders
VS Code Add Cursors in selection:             Alt-Shift-I
VS Code Open Terminal:                        Alt-F12
VS Code Open Terminal in Folder:              Select Folder + Cmd-Alt-F12
VS Code Open Keymap:                          Cmd-K Cmd-S
VS Code Execute File (Code Runner Plugin):    Ctrl-Alt-N
VS Code Toggle Secondary (Right) Sidebar:     Cmd-Alt-B
VS Code Go To Symbol in Workspace:            Cmd-O

Eclipse Show Javadoc:                         F2
Eclipse Format Source Code (ex. Imports):     Cmd-Shift-F
Eclipse Organize Imports:                     Cmd-Shift-O
Eclipse Find in Files:                        Ctrl-H
Eclipse Toggle Comment:                       Cmd-7
Eclipse Create new instance:                  open -na eclipse
Eclipse Open Terminal:                        Ctrl-Alt-T

Install unsigned DMG/PKG App:                 xattr -d com.apple.quarantine <NAME>.pkg
Show Python installation path:                python -m site
Change default app for file extension:        Right click file in Finder -> Information -> Name & Suffix 
                                              -> Change App and click "Change for all"
Ctrl+Space default:                           switch input sources 
                                              (disable under: Preferences>Keyboard>Shortcuts>Input Sources)
```

## Files

* [env.sh](env.sh) - Shell startup script
* [toggle_screen_resolution.sh](toggle_screen_resolution.sh) - Shell script assigned to a custom Keyboard Shortcut

## Useful Applications

### Google

* Homebrew
* Oh my ZSH
* VS Code
* IntelliJ IDEA
* [serviceman](https://webinstall.dev/serviceman/) (systemd, launchd manager)
* Karabiner-Elements (Keyboard customization): <https://karabiner-elements.pqrs.org/>
* Rectangle (Snap windows to the left, right, etc.)
* KeepingYouAwake
* [Swift Quit](https://swiftquit.com/) (Close Mac Applications Automatically When Their Windows Close)

### Homebrew

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

* Open Terminal here: <https://github.com/jbtule/cdto#cd-to>
* Right click -> New File: <https://apps.apple.com/in/app/new-file-menu-free/id1066302071?mt=12>
* display_manager.py: <https://github.com/univ-of-utah-marriott-library-apple/display_manager>
  cp /Library/Python/2.7/site-packages/display_manager_lib.py /Library/Frameworks/Python.framework/Versions/3.11/lib/python3.11/site-packages/ && pip install pyobjc
