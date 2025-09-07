package pl.atd.ollama.confi;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

  @Bean
  public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {

    return chatClientBuilder
        .defaultOptions(
            ChatOptions.builder()
                .temperature(9.9)
                .build()
        )
        .defaultAdvisors(new SimpleLoggerAdvisor())
        .defaultUser("How can you help me?")
        .build();
  }
}
