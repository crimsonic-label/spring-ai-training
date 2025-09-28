package pl.atd.mcpserverstdio.config;

import java.util.List;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.atd.mcpserverstdio.tool.HelpDeskTools;

@Configuration
public class McpServerConfig {

  // point what tools will be available in server
  @Bean
  List<ToolCallback> toolCallbacks(HelpDeskTools helpDeskTools) {
    return List.of(ToolCallbacks.from(helpDeskTools));
  }
}
