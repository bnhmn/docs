# Certificates

Certificates are digital documents that link a public key to an identity (like a website, organization, or individual).
They are mainly used to authenticate identities and secure digital communication (e.g., HTTPS).

## How Certificates Work

1. A Certificate Authority (CA) issues a certificate, vouching for the identity of the certificate holder.
2. When you connect to a server (like via HTTPS), your system checks the certificate chain up to a trusted CA.
3. If valid, your system can securely exchange data using the public key (encryption) and verify integrity
  (digital signatures).

## Important Terms

| Term | Description |
|------|-------------|
| Certificate Authority (CA) | An organization that issues certificates, asserting that the public key belongs to the stated entity. |
| Certificate | A digital form of identification, like a passport, which contains the holder's public key. |
| Public Key | A key that can be shared openly; it’s used to encrypt data that only the matching private key can decrypt. |
| Private Key | A key that must be kept secure by the owner; it’s used to decrypt data encrypted with the public key. |
| Truststore | A repository storing trusted certificates (usually CA certificates) used to verify others' certificates. |
| Keystore | A repository containing certificates **and** their private keys, used to authenticate a system and secure data. |

### Analyze a Certificate File

PEM certificate (.pem, .crt, .cer):

```bash
openssl x509 -in cert.pem -text -noout
```

DER certificate (.der):

```bash
openssl x509 -in cert.der -inform der -text -noout
```

PKCS#7 (.p7b):

```bash
openssl pkcs7 -in cert.p7b -print_certs -text -noout
```

PKCS#12 (.p12, .pfx):

```bash
openssl pkcs12 -in cert.p12 -info -nodes
```


## Certificate Formats

### X.509

[X.509](https://en.wikipedia.org/wiki/X.509) is a standard that defines the format of **public key certificates**.
It specifies what information a certificate must contain (like the subject, issuer, validity period, and public key)
and how it is digitally signed by a Certificate Authority (CA).

X.509 certificates are the foundation of TLS/SSL, HTTPS, and many authentication systems, allowing parties to verify
identities and establish secure communication.

### PEM (Privacy-Enhanced Mail)

A text-based format that encodes X.509 certificates and keys in [Base64](https://en.wikipedia.org/wiki/Base64) with
BEGIN and END markers. It is widely used for web servers and OpenSSL. PEM files can contain certificates, private keys,
or both, but usually, the private key is stored separately.

Common file extensions: `.pem`, `.crt`, `.cer`, `.key`.

```text
-----BEGIN CERTIFICATE-----
R81cjvMyFHRTvHIGSdFdYwpcbZOijArjPERCcb6rAKyEhD8YYHFMJ992xp0He74d
V3VQqAfryBZSHM56NxmsORgADfGTsWPQZy1mDCkDRAMq0hIHUMtvccG6ilEgvQwS
qm6YtnBuVFtNtfIcmhD07DGvLfd8Q4UMeLRr7n4ODxrWeN3EGVkJBW0rGSqbqaya
...
-----END CERTIFICATE-----
```

### DER (Distinguished Encoding Rules)

A binary format often used in Java environments or Windows systems to encode X.509 certificates and keys.
DER files typically contain a single certificate or key.

Common file extensions: `.der`, `.cer`.

### PKCS#12 (PFX/P12)

A binary format that can bundle an X.509 certificate, its private key, and any intermediate certificates into a single
file. It is commonly used for transferring certificates with their private keys between systems.
It is password-protected and the the only common format that can safely store private keys along with certificates.

Common file extensions: `.p12`, `.pfx`.

```text
cert.p12
 ├─ Private Key
 ├─ Leaf Certificate
 └─ Intermediate CA(s)
```
