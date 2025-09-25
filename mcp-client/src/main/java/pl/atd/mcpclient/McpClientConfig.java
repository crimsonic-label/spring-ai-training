package pl.atd.mcpclient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpClientConfig {

  @Bean("chatClient")
  public ChatClient chatClient(ChatClient.Builder chatClientBuilder,
      ToolCallbackProvider toolCallbackProvider) {

    return chatClientBuilder
        .defaultToolCallbacks(toolCallbackProvider.getToolCallbacks())
        .defaultAdvisors(new SimpleLoggerAdvisor())
        .build();
  }
}
