# Authentication

## Authentication Strategies for SPAs with Corporate IdP

Choosing the right authentication architecture for a Single Page Application (SPA) requires balancing security (protection against XSS and CSRF), user experience (preventing forced logouts or flickering redirects), and implementation complexity. In an enterprise environment using a central Identity Provider (IdP) with JWTs, the following patterns are the most common:

| Approach | Storage (Browser) | Security | UX | Complexity | CSRF Protection Required? |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **BFF (Server-side Session)** | Session-ID Cookie (HttpOnly) | **Excellent** | **Excellent** (Immediate) | High (Redis/DB needed) | Yes |
| **BFF (Encrypted Cookie)** | JWT + Refresh Token in Cookie | **High** | **Excellent** (Immediate) | Medium | Yes |
| **Stateless JWT Cookie** | JWT only in Cookie (HttpOnly) | Medium | Medium (Logout on expiry) | Low | Yes |
| **In-Memory (SPA)** | JS Variable (RAM) | Medium | **Poor** (Redirects/Hacks) | Medium | No |
| **LocalStorage (SPA)** | LocalStorage (Plaintext) | **Poor** | Good | Very Low | No |


### Key Takeaways

* **BFF (Backend-for-Frontend):** This is the gold standard for security. By moving token management to a server-side component, you hide sensitive JWTs from the browser's JavaScript, effectively neutralizing XSS-based token theft.
* **Encrypted Cookies:** This "stateless" BFF variant can be a good compromise. It provides a seamless UX (no flickering on page refresh) without the need for a server-side database like Redis, as long as the tokens fit within the 4KB cookie limit.
* **CSRF Note:** Whenever you transition from Authorization Headers to Cookies, you **must** implement CSRF protection. Modern `SameSite=Lax` or `Strict` attributes provide a good baseline, but a dedicated Anti-CSRF-Token or a Custom Header check is recommended for "Defense in Depth".

See also [OAuth 2.0 Patterns for Browser-Based Applications](https://datatracker.ietf.org/doc/html/draft-ietf-oauth-browser-based-apps#name-application-architecture-pa).
