package pl.atd.ollama.confi;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

  @Bean
  public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
    return chatClientBuilder
        .defaultUser("How can you help me?")
        .build();
  }
}
