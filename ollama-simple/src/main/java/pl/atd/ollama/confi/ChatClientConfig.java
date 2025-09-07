package pl.atd.ollama.confi;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.atd.ollama.advisor.TokenUsageAuditAdvisor;

@Configuration
public class ChatClientConfig {

  @Bean
  public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
    return chatClientBuilder
        .defaultAdvisors(new SimpleLoggerAdvisor(), new TokenUsageAuditAdvisor())
        .defaultUser("How can you help me?")
        .build();
  }
}
