# macOS

[macOS](https://en.wikipedia.org/wiki/MacOS) is an operating system developed by
[Apple Inc](https://www.apple.com/) known for its user-friendly interface and sleek design. It
offers a robust application ecosystem, seamless integration with other Apple products, and a strong
emphasis on security. macOS is popular among both everyday users and creative professionals.

## Useful Shortcuts

See [shortcuts.md](shortcuts.md).

## Useful Applications

### Via Google

- [Homebrew](https://brew.sh/)
- [Oh my ZSH](https://ohmyz.sh/#install)
- [VS Code](https://code.visualstudio.com/download)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/download/?section=mac)

Optional:

- [Rectangle](https://rectangleapp.com/) (Snap windows to the left, right, etc.)
- [KeepingYouAwake](https://keepingyouawake.app/) (Prevent the MacBook from going into sleep mode)
- [serviceman](https://webinstall.dev/serviceman/) (systemd, launchd manager)
- [Karabiner-Elements](https://karabiner-elements.pqrs.org/) (keyboard customizer)

### Via Homebrew

```bash
apps=(
  openjdk@25
  maven
  python3
  node
  docker
  fzf              # fuzzy finder
  htop             # process viewer
  watch            # run commands every n seconds
  coreutils        # timeout, md5sum, sha256sum
  colima           # docker daemon - brew services start colima && colima delete && colima start --memory 12 --cpu 10
  ccat             # colorized cat
  z                # warp into directory
  nano             # latest nano with more features than macOS default
  romkatv/powerlevel10k/powerlevel10k     # zsh theme
)
brew install "${apps[@]}"
```

### Terminal Theme

- [Basic Dark.terminal](./Basic%20Dark.terminal)

### Scripts

- [.zshrc](.zshrc) - Shell startup script
- [.env.sh](.env.sh) - Shell environment config
- [.nanorc](.nanorc) - nano editor config
- [cdto](https://github.com/jbtule/cdto#cd-to) - Open Terminal here
- [New File Menu Lite](https://apps.apple.com/in/app/new-file-menu-free/id1066302071?mt=12) - Right
  click->New File

## Build and Run Docker Containers

Use [colima](https://github.com/abiosoft/colima). Works with docker compose and buildx too:
<https://aosolorzano.medium.com/1ce8b3bae158>

## Run Ubuntu VM

Use [multipass](https://multipass.run/install).

```bash
multipass list
```
