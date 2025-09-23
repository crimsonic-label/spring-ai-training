package pl.atd.toolcalling;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import pl.atd.toolcalling.tools.TimeTools;

@Configuration
public class HelpDeskChatClientConfig {

  @Value("classpath:/promptTemplates/helpDeskSystemPromptTemplate.st")
  private Resource systemPromptTemplate;

  @Bean("helpDeskChatClient")
  public ChatClient chatClient(ChatClient.Builder chatClientBuilder, TimeTools timeTools) {

    return chatClientBuilder
        .defaultSystem(systemPromptTemplate)
        .defaultTools(timeTools)
        .defaultAdvisors(new SimpleLoggerAdvisor())
        .build();
  }
}
