package pl.atd.subtitles;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

  @Bean
  public ChatMemory chatMemory(ChatMemoryRepository chatMemoryRepository) {
    return new SubtitlesChatMemory(chatMemoryRepository, 3);
  }

  @Bean
  public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
    return chatClientBuilder
        .defaultAdvisors(
            new SimpleLoggerAdvisor(),
            MessageChatMemoryAdvisor.builder(chatMemory).build())
        .build();
  }
}
