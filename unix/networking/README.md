# Networking

## Check Connectivity Between Devices in the Cloud

These commands help diagnose network connectivity between cloud VMs, databases, and other devices. 
They check basic host reachability and access to a specific TCP port.

Assume you are logged in to a VM and want to check whether you can access your PostgreSQL database at
its private IP address `10.127.0.3` on port `5432`.

You can use ping to do a simple connectivity check:

```bash
ping -c 4 10.127.0.3
```

However, ping uses ICMP to check whether the target responds and this is often blocked by cloud or host firewalls.
A failed ping does not necessarily mean that the destination is unreachable.

If ping is unsuccessful, try if it's possible to open a TCP connection to the desired host and target:

```bash
timeout 5 bash -c '</dev/tcp/10.127.0.3/5432' && echo "Port reachable" || echo "Port not reachable"
```
