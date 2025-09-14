package pl.atd.rag.controller;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
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
  private final VectorStore vectorStore;

  @Value("classpath:/promptTemplate/systemPromptRandomDataTemplate.st")
  Resource promptTemplate;

  @Value("classpath:/promptTemplate/boardgameSystemPromptTemplate.st")
  Resource boardgameTemplate;

  @GetMapping("/random/chat")
  public ResponseEntity<String> randomChat(@RequestParam("message") String message) {
    return ResponseEntity.ok(chatClient.prompt()
        .user(message)
        .call().content());
  }

  @GetMapping("/boardgame/chat")
  public ResponseEntity<String> documentChat(@RequestParam("message") String message) {
    return ResponseEntity.ok(chatClient.prompt()
        .user(message)
        .call().content());
  }
}
