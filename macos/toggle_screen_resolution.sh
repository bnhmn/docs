#!/bin/zsh

# This script toggles the screen resolution of the connected external Monitor
# between 3440:1440px and 1920:1080px (which is better for screen sharing in Meetings).

# As a prerequisite, install the display_manager tool: https://github.com/univ-of-utah-marriott-library-apple/display_manager
# And run these terminal commands:
# cp /Library/Python/2.7/site-packages/display_manager_lib.py /Library/Frameworks/Python.framework/Versions/3.11/lib/python3.11/site-packages/ 
# pip install pyobjc

# The script may then be used in a MacOS Shortcut ("Kurzbefehle") as script action.
# While being in the MacOS Shortcut editing view, press on the info-button in the upper right corner,
# enable checkbox "Use as Shortcut" and enter a keyboard shortcut in the text box.

display_info="$(/usr/local/bin/display_manager.py show)"

if [[ "$display_info" =~ "ext0" ]]; then
  display=ext0
else
  display=main
fi

if [[ "$display_info" =~ "3440x1440" ]]; then
  /usr/local/bin/display_manager.py res 1920 1080 50 "$display"
else
  /usr/local/bin/display_manager.py res 3440 1440 50 "$display"
fi
