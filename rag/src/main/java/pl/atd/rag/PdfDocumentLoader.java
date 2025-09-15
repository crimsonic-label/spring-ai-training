package pl.atd.rag;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PdfDocumentLoader {

  private final VectorStore vectorStore;

  @Value("${atd.rag.documents.location-pattern}")
  private String locationPattern;

  /**
   * PDF document data for Vector store loaded when app starts Read pdf document with Apache Tika
   * library
   */
  @PostConstruct
  public void loadPdf() {
    TokenTextSplitter textSplitter = TokenTextSplitter.builder()
        .withChunkSize(100).withMaxNumChunks(400)
        .build();

    try {
      // list of prf resources in directory
      List<Resource> directoryResources = DocumentUtil.getDirectoryResources(locationPattern);
      log.info("Resources found in {}: {}", locationPattern, directoryResources);

      // read pdf as document, split and add to vector store
      directoryResources.stream()
          .map(res -> new TikaDocumentReader(res).get())
          .forEach(documents -> vectorStore.add(textSplitter.split(documents)));

    } catch (IOException e) {
      log.error("Cannot read document resources from path: {}", locationPattern);
    }
  }
}
