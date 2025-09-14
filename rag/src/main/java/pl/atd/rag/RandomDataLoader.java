package pl.atd.rag;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RandomDataLoader {

  private final VectorStore vectorStore;

  /**
   * Data for Vector store loaded when app starts and when bean was created
   */
  @PostConstruct
  public void loadSentencesIntoVectorStore() {
    try (InputStream resource = getClass().getClassLoader().getResourceAsStream("textToBeLoaded.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8))) {

      vectorStore.add(bufferedReader.lines()
          .map(Document::new)
          .toList());

    } catch (IOException e) {
      log.error("Cannot get texts from resource file to be loaded.", e);
    }
  }
}
