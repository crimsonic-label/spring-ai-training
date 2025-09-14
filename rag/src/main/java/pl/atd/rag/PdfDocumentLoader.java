package pl.atd.rag;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
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

  @Value("classpath:7-Wonders-Rulebook-EN.pdf")
  Resource documentFile;

  /**
   * PDF document data for Vector store loaded when app starts
   * Read pdf document with Apache Tika lbrary
   */
  @PostConstruct
  public void loadPdf() {
    List<Document> documents = new TikaDocumentReader(documentFile).get();

    TokenTextSplitter textSplitter = TokenTextSplitter.builder()
        .withChunkSize(100).withMaxNumChunks(400)
        .build();
    vectorStore.add(textSplitter.split(documents));
  }
}
