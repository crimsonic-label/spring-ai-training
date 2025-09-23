package pl.atd.toolcalling.controller;

import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.atd.toolcalling.tools.HelpDeskTools;

@RestController
@RequestMapping("/api/tools")
public class HelpDeskController {

  private final ChatClient chatClient;
  private final HelpDeskTools helpDeskTools;

  public HelpDeskController(@Qualifier("helpDeskChatClient") ChatClient chatClient,
      HelpDeskTools helpDeskTools) {
    this.chatClient = chatClient;
    this.helpDeskTools = helpDeskTools;
  }

  @GetMapping("/help-desk")
  public ResponseEntity<String> helpDesk(
      @RequestHeader("username") String username,
      @RequestParam("message") String message) {

    return ResponseEntity.ok(chatClient.prompt()
        .user(message)
        .tools(helpDeskTools)
        .toolContext(Map.of("username", username))
        .call()
        .content());
  }
}
