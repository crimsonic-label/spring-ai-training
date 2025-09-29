package pl.atd.rag.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class ChatClientConfig {

  @Value("classpath:/promptTemplate/boardgameSystemPromptTemplatePl.st")
  private Resource boardgameTemplate;

  @Bean
  public ChatClient chatClient(ChatClient.Builder chatClientBuilder,
      RetrievalAugmentationAdvisor retrievalAugmentationAdvisor) {
    return chatClientBuilder
        .defaultAdvisors(new SimpleLoggerAdvisor(), retrievalAugmentationAdvisor)
        .defaultSystem(boardgameTemplate)
        .build();
  }

  @Bean
  public RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore) {
    return RetrievalAugmentationAdvisor.builder().documentRetriever(
        VectorStoreDocumentRetriever.builder()
            .vectorStore(vectorStore)
            .topK(3)
            .similarityThreshold(0.5)
            .build()
    ).build();
  }
}
