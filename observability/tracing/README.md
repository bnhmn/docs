# Tracing

In this context, tracing means observing how an HTTP request travels through connected systems to
identify errors or performance bottlenecks.

Frameworks like [OpenTelemetry](https://opentelemetry.io/docs/what-is-opentelemetry/) implement tracing.
They can log and propagate tracing metadata and, if desired, export it to a tracing backend.

See also:
[Spring Boot Tracing](https://docs.spring.io/spring-boot/reference/actuator/tracing.html),
[Micrometer Tracing](https://docs.micrometer.io/tracing/reference/).

## Terminology

Tracing frameworks distinguish between the terms Trace ID and Span ID.

| Name     | Description                                                    | Example                          |
|----------|----------------------------------------------------------------|----------------------------------|
| Trace ID | a globally unique identifier for a distributed trace (request) | 4bf92f3577b34da6a3ce929d0e0e4736 |
| Span ID  | an identifier for a single operation within a trace            | 00f067aa0ba902b7                 |

## How it Works

1. The system extracts the `Trace ID` and `Span ID` from the incoming request (if present).
2. The system creates a new span using the received `Span ID` as its parent.
3. For any outgoing requests, the system injects the `Trace ID` and the new `Span ID` into the
   request.
4. If enabled, the system sends span data (timing, status, attributes) to a backend like Jaeger or
   Zipkin, enabling developers to visualize and analyze traces.

## traceparent Header

The [W3C Trace Context](https://www.w3.org/TR/trace-context/) specification defines a standard way
to propagate tracing metadata across services using the [traceparent](https://www.w3.org/TR/trace-context/#traceparent-header)
HTTP header. By default, [OpenTelemetry](https://opentelemetry.io/docs/concepts/context-propagation/)
uses this header to propagate tracing metadata across systems.

### Example

```yaml
traceparent: 00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01
```

### Format

```yaml
traceparent: {version}-{trace-id}-{parent-id}-{trace-flags}
```

| Field       | Description                                                                   |
|-------------|-------------------------------------------------------------------------------|
| version     | Version of the Trace Context specification. Always `00` for current standard. |
| trace-id    | Globally unique identifier for the trace across all spans and services.       |
| parent-id   | Identifier of the parent span (the immediate caller).                         |
| trace-flags | Flags controlling trace options, e.g. `01` = sampled, `00` = not sampled.     |
