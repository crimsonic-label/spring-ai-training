package pl.atd.rag.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rag")
@RequiredArgsConstructor
public class RagController {

  private final ChatClient chatClient;

  @GetMapping("/boardgame/chat")
  public ResponseEntity<String> boardgamesChat(@RequestParam("message") String message) {
    return ResponseEntity.ok(chatClient.prompt()
        .user(message)
        .call().content());
  }
}
