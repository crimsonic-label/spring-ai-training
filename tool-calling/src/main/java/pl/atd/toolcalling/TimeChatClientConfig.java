package pl.atd.toolcalling;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.atd.toolcalling.tools.TimeTools;

@Configuration
public class TimeChatClientConfig {

  @Bean
  public ChatClient chatClient(ChatClient.Builder chatClientBuilder, TimeTools timeTools) {

    return chatClientBuilder
        .defaultTools(timeTools)
        .defaultAdvisors(new SimpleLoggerAdvisor())
        .build();
  }
}
