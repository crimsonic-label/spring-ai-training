package pl.atd.ollama.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatMemoryController {

  private final ChatClient chatClient;

  public ChatMemoryController(@Qualifier("memoryChatClient") ChatClient chatClient) {
    this.chatClient = chatClient;
  }

  @GetMapping("/chat-memory")
  public ResponseEntity<String> chatMemory(
      @RequestHeader("username") String username,
      @RequestParam("message") String message) {

    return ResponseEntity.ok(chatClient.prompt()
        .user(message)
        // parameter for advisors - for chat memory advisor to split chat memory for different users
        .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, username))
        .call()
        .content());
  }
}
