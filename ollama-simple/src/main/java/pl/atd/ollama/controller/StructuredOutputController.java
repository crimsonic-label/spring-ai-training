package pl.atd.ollama.controller;

import java.util.List;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.atd.ollama.model.LocationAnimals;

@RestController
@RequestMapping("/api")
public class StructuredOutputController {

  private final ChatClient chatClient;

  public StructuredOutputController(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.defaultAdvisors(new SimpleLoggerAdvisor()).build();
  }

  @GetMapping("/chat-bean")
  public ResponseEntity<LocationAnimals> chatBean(@RequestParam("message") String message) {
    return ResponseEntity.ok(chatClient.prompt(message).call().entity(LocationAnimals.class));
  }

  @GetMapping("/chat-list")
  public ResponseEntity<List<String>> chatList(@RequestParam("message") String message) {
    return ResponseEntity.ok(chatClient.prompt(message).call().entity(new ListOutputConverter()));
  }

  @GetMapping("/chat-map")
  public ResponseEntity<Map<String, Object>> chatMap(@RequestParam("message") String message) {
    return ResponseEntity.ok(chatClient.prompt(message).call().entity(new MapOutputConverter()));
  }

  @GetMapping("/chat-bean-list")
  public ResponseEntity<List<LocationAnimals>> chatBeanList(@RequestParam("message") String message) {
    return ResponseEntity.ok(chatClient.prompt(message).call().entity(new ParameterizedTypeReference<List<LocationAnimals>>(){}));
  }
}
