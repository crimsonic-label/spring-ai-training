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