# Networking

Networks connect computers so they can reliably exchange data.

## Internet routing basics

The [Internet](https://en.wikipedia.org/wiki/Internet) is not a single network in itself, but rather
an interconnection of many different networks. Many other private networks are isolated from it or
connect to it only through a gateway.

Devices use [IP addresses](https://en.wikipedia.org/wiki/IP_address) as network destinations: like
postal addresses, they help [routers](<https://en.wikipedia.org/wiki/Router_(computing)>) forward
packets to their destination. A device can have multiple network interfaces
([NICs](https://en.wikipedia.org/wiki/Network_interface_controller)) through which it connects to
multiple networks, and can have both private IP addresses (which are not accessible from the
Internet) and public IP addresses (which are accessible from the Internet).

A public IP address normally identifies exactly one destination. However, there are exceptions. With
[Anycast](https://en.wikipedia.org/wiki/Anycast), a single address refers to multiple locations, and
a request is delivered to the closest location of them. With
[Multicast](https://en.wikipedia.org/wiki/IP_multicast), a single address refers to a group of
recipients, and a packet sent to this address is automatically distributed to all subscribed
members.

Private IPv4 addresses ([RFC 1918](https://www.rfc-editor.org/rfc/rfc1918.html)) are intended for
internal networks and are not routed across the public Internet; they can be reused in separate
networks:

```text
- 10.0.0.0/8      (10.0.0.0 - 10.255.255.255)
- 172.16.0.0/12   (172.16.0.0 - 172.31.255.255)
- 192.168.0.0/16  (192.168.0.0 - 192.168.255.255)
```

Devices do not need public IP addresses to access the Internet: they can use a
[NAT](https://en.wikipedia.org/wiki/Network_address_translation) gateway to translate their private
addresses to a public address, effectively allowing multiple devices to share a single public IP.

The [CIDR](https://en.wikipedia.org/wiki/Classless_Inter-Domain_Routing) notation describes an IP
network as an address and a prefix length. For example, `192.168.1.0/24` means the first 24 bits
identify the network; the remaining bits identify addresses within it. Routers use these prefixes to
group destinations and choose routes.

[Routing](https://en.wikipedia.org/wiki/Routing) connects independently operated networks called
[autonomous systems (AS)](<https://en.wikipedia.org/wiki/Autonomous_system_(Internet)>). Routers at
network borders use the
[Border Gateway Protocol (BGP)](https://en.wikipedia.org/wiki/Border_Gateway_Protocol) to
communicate with neighboring networks. They exchange reachability announcements that describe which
IP prefixes can be reached through them. This allows networks to learn routes to destinations across
the Internet. Routers exchange updates over long-lived BGP sessions using TCP port 179.

## Global load balancing

Critical applications such as online banking must remain available when an entire data center fails
(e.g., due to a power outage, a network outage, or a regional disaster). Redundant deployments in
multiple regions protect against such failures. Global load balancing directs new traffic to a
healthy region so the service can continue running. Two common approaches are Anycast routing and
DNS-based load balancing.

### Anycast

[Anycast](https://en.wikipedia.org/wiki/Anycast) lets multiple service locations provide a service
through the same IP address. A point of presence
([PoP](https://en.wikipedia.org/wiki/Point_of_presence)) at each location announces the same IP
prefix. BGP usually routes traffic to the topologically nearest PoP according to network routing
policies. If monitoring detects a PoP failure, the provider can withdraw that PoP's route. After BGP
converges, routers use an available route to another service location. However, convergence takes
time, and existing connections may be interrupted.

### DNS-based global load balancing

With [DNS](https://en.wikipedia.org/wiki/Domain_Name_System)-based load balancing, a DNS provider
manages the IP addresses of service endpoints in multiple locations. It checks endpoint health at
configured intervals and excludes unhealthy locations from its DNS responses. When a client looks up
the service name, the provider returns the address of a healthy service location, optionally
selected by latency, geography, or another policy. However, DNS resolvers cache answers for a period
of time (TTL), so some clients may keep using an old address until the cache expires; existing
connections may be interrupted.

## Check connectivity between devices

Assume you are logged in to a VM and want to check whether you can access a PostgreSQL database at
its private IP address `10.127.0.3` on port `5432`.

### ping

You can use ping to do a simple connectivity check:

```bash
ping -c 4 10.127.0.3
```

However, ping uses ICMP to check whether the target responds and this is often blocked by cloud or
host firewalls. A failed ping does not necessarily mean that the destination is unreachable.

### tcp

If ping is unsuccessful, try if it's possible to open a TCP connection to the desired host and
target:

```bash
timeout 5 bash -c '</dev/tcp/10.127.0.3/5432' && echo "Port reachable" || echo "Port not reachable"
```

If `timeout` is not available on your system (e.g., macOS), simply remove the timeout:

```bash
bash -c '</dev/tcp/10.127.0.3/5432' && echo "Port reachable" || echo "Port not reachable"
```
