package pl.atd.mcpclient.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class McpClientController {

  private final ChatClient chatClient;

  @GetMapping("/chat")
  public String chat(@RequestParam("message") String message) {
    return chatClient.prompt().user(message)
        .call().content();
  }
}
