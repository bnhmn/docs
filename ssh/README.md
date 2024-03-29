# SSH

[Secure Shell (SSH)](https://en.wikipedia.org/wiki/Secure_Shell) is a protocol
that allows you to connect to a remote computer / server and execute commands
in a terminal as if you were directly connected to the computer.

## Create an SSH Key on Local Machine

To be able to connect to a remote computer via SSH, you need to first create a public-private key pair
for your own computer (SSH client).
The key pair should be created directly on and used exclusively for that computer.
The private key **must not be shared** with any other party.

You can create an SSH key pair with the recommended `ED25519` algorithm using this command:

```bash
ssh-keygen -t ed25519 -C "Desktop PC of john.doe@gmail.com"
```

1. You should give the key a descriptive comment, so you and others can quickly identify which device this key
   belongs to. In this example, we use `Desktop PC of john.doe@gmail.com`.
2. You will be asked where the key files should be stored. Use the default value provided such as `~/.ssh/id_ed25519`.
3. You will be asked if you want to protect the private key with a passphrase. This password must then be provided
   in addition to the private key when attempting to connect. But this is optional.

If everything has worked, there should now be two files in your `~/.ssh` directory:

* `id_ed25519` - The private key - must not leave the system!
* `id_ed25519.pub` - The public key.

<!-- markdownlint-disable MD033 -->
<details>
<summary>RSA algorithm</summary>

Some older systems only support the `RSA` algorithm. You can create such a key pair with this command:

```bash
ssh-keygen -t rsa -b 4096 -C "Desktop PC of john.doe@gmail.com"
```

</details>

## Authorize an SSH Key on Remote Machine

To be able to connect to the remote computer, the public key from your own computer (SSH client)
must be registered on the remote computer (SSH server).

* If the remote computer is operated by a cloud provider, you can usually add the public key
  via the cloud provider's web interface.
* If the remote computer is operated by another person/company/department, you must forward your public key to the
  person responsible for authorizing keys.
* If you already have access to the remote computer, you can authorize your SSH key by yourself.
  
To authorize an SSH key by yourself, log in with your username (e.g. `ubuntu`) on the remote computer and open a terminal
(either via SSH or via the device's monitor and keyboard).

Copy the content of your public key file `~/.ssh/id_ed25519.pub` into the clipboard. It should look like this:

```text
ssh-ed25519 AAAAC3NzaCJNDI1NTE5AAAAIHg7QdYXCP7mBwlMLuZZLarrQK5S4nG7CMTZOvk4mikY Desktop PC of john.doe@gmail.com
```

Add your public key to the list of authorized keys by executing the following command on the remote computer:

```bash
echo "<INSERT-PUBLIC-KEY-CONTENT-HERE>" >> ~/.ssh/authorized_keys
```

Now your local computer is authorized to connect via SSH as user `ubuntu` with the remote computer.

## Connect to the Remote Machine

Execute the following command to connect as user `ubuntu` to the remote computer which has the IP address `90.119.5.44`:

```bash
ssh ubuntu@90.119.5.44
```

While connecting, your local computer uses your private key from the `~/.ssh` folder to authenticate itself
to the remote computer. The remote computer checks if you are allowed to connect by verifying if your
public key is included in the list of authorized keys found in `~/.ssh/authorized_keys`.

If everything worked, you should now be connected to a terminal on your remote computer and be able to execute
commands there.

If you are running **Windows** and want to connect to a remote machine via SSH,
use [PuTTY](https://www.putty.org/) instead.
