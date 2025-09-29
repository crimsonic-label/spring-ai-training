# Use actuator and its metrics

All matrics available in actuator
http://localhost:8080/actuator/metrics

Vector client operations (add and query)
- http://localhost:8080/actuator/metrics/db.vector.client.operation

Token usage:
- http://localhost:8080/actuator/metrics/gen_ai.client.token.usage
- http://localhost:8080/actuator/metrics/gen_ai.client.token.usage?tag=gen_ai.token.type:output

Advisors usage:
- http://localhost:8080/actuator/metrics/spring.ai.advisor
- http://localhost:8080/actuator/metrics/spring.ai.advisor?tag=spring.ai.advisor.name:RetrievalAugmentationAdvisor

Communications from chat to LLM
- http://localhost:8080/actuator/metrics/spring.ai.chat.client

# Expose metrics to prometheus
- add `io.macrometer` `micrometer-registry-prometheus` dependency - library that transforms metrics format
- add `prometheus` to `management.endpoints.web.exposure.include` in `application.properties`

Prometheus data should be found in actuator:
http://localhost:8080/actuator

- run Prometheus with docker - add service to docker compose.yaml
- add Prometheus config `prometheus-config.yaml`
- Prometheus is available at `http://localhost:9090/query`

# Add Grafana
- add service to docker.yml
- open http://localhost:3000, log with `admin` `admin`
- in connections menu add Prometheus data source

  - url: http://prometheus:9090 (this is inside docker spring-ai network)
  
- build Grafana dashboard, create one 
- add visualization, 
- select prometheus data source
- select metrics:

  - gen_ai_client_token_usage_total,
  - label filters: gen_ai_token_type
  - total

# Tracing AI operations

- add dependency: 

  - `io.micrometer:micrometer-tracing-bridge-otel` to expose data in open telemetry format
  - `io.opentelemetry:opentelemetry-exporter-otlp` to export tracing information to collector component
  - create config class `OpenTelemetryExporterConfig'
  - create exporter bean for url to run exporter component
  - add tracing service with Jaeger docker with the same port as exporter
  - call http://localhost:16686/search
  - search for service 'RAG' and operations