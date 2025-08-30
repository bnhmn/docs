# Postgres

[PostgreSQL](https://www.postgresql.org/) is a free and open-source database system. It allows you to store and retrieve
data in structured tables. It's known for being reliable, robust, and flexible, with wide usage in many web and software
applications.

## Documentation

See <https://www.postgresql.org/docs/current/>.

## Examples

### [Partial Index](https://www.postgresql.org/docs/current/indexes-partial.html)

```sql
CREATE UNIQUE INDEX user_active_idx
    ON user(user_id)
    WHERE state = 'ACTIVE';
```
