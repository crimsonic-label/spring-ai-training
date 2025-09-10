package pl.atd.ollama.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatMemoryClientConfig {

  @Bean
  public ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
    // configure chat memory to store only 10 messages
    return MessageWindowChatMemory.builder()
        .maxMessages(10)
        .chatMemoryRepository(jdbcChatMemoryRepository)
        .build();
  }

  @Bean("memoryChatClient")
  public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
    return chatClientBuilder
        .defaultAdvisors(
            new SimpleLoggerAdvisor(),
            MessageChatMemoryAdvisor.builder(chatMemory).build())
        .build();
  }
}
