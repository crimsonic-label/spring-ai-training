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