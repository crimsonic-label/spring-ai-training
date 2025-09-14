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

  @GetMapping("/random/chat")
  public ResponseEntity<String> randomChat(@RequestParam("message") String message) {
    // top 3 documents from query, get documents with minimum probability of 0.5
    SearchRequest searchRequest = SearchRequest.builder().query(message).topK(3)
        .similarityThreshold(.5).build();
    // search for documents
    List<Document> similarDocuments = vectorStore.similaritySearch(searchRequest);
    // connect documents to single string
    String similarContext = similarDocuments.stream().map(Document::getText)
        .collect(Collectors.joining(System.lineSeparator()));

    return ResponseEntity.ok(chatClient.prompt()
        // system prompt with template filled with found documents
        .system(promptSystemSpec -> promptSystemSpec.text(promptTemplate)
            .param("documents", similarContext))
        .user(message)
        .call().content());
  }
}
