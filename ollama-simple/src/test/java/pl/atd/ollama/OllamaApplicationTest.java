package pl.atd.ollama;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.Timeout;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.Builder;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;
import pl.atd.ollama.controller.ChatController;
import pl.atd.ollama.controller.PromptStuffingController;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@TestPropertySource(properties = {
    "spring.ai.model.chat=ollama",
    "spring.ai.ollama.chat.options.model=mistral",
    "logging.level.org.springframework.ai=DEBUG"
})
class OllamaApplicationTest {

  @Autowired
  private ChatController chatController;

  @Autowired
  private PromptStuffingController promptStuffingController;

  @Autowired
  private ChatModel chatModel;

  @Value("${test.relevancy.min-score:0.7}")
  private float minRelevancyScore;

  @Value("classpath:/promptTemplates/systemPromptTemplate.st")
  private Resource hrPolicyTemplate;

  private ChatClient chatClient;
  private RelevancyEvaluator relevancyEvaluator;
  private FactCheckingFixedEvaluator factCheckingEvaluator;

  @BeforeEach
  void setup() {
    Builder builder = ChatClient.builder(chatModel)
        .defaultAdvisors(new SimpleLoggerAdvisor());
    this.chatClient = builder.build();
    this.relevancyEvaluator = new RelevancyEvaluator(builder);
    this.factCheckingEvaluator = new FactCheckingFixedEvaluator(builder);
  }

  @Test
  @DisplayName("Should return relevant response for basic geography question")
  @Timeout(value = 30)
  void evaluateChatControllerResponseRelevancy() {
    // given
    String question = "Jakie są dzielnice Warszawy?";
    // when
    String aiResponse = chatController.chat(question);
    // then
    EvaluationRequest evaluationRequest = new EvaluationRequest(question, aiResponse);
    EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);

    Assertions.assertAll(
        () -> assertThat(aiResponse).isNotBlank(),
        () -> assertThat(evaluationResponse.getScore()).withFailMessage("""
                ================================================
                The score %.2f is lower than the minimum required %.2f.
                Question: "%s"
                Response: "%s"
                ================================================
                """, evaluationResponse.getScore(), minRelevancyScore, question, aiResponse)
            .isGreaterThan(minRelevancyScore),
        () -> assertThat(evaluationResponse.isPass()).withFailMessage("""
                ================================================
                The answer was not considered relevant.
                Question: "%s"
                Response: "%s"
                ================================================
                """, question, aiResponse)
            .isTrue()
    );
  }

  @Test // rather use different model to validate, FactCheckingEvaluator is rather for RAG
  @DisplayName("Should return correct response for gravity-related question")
  @Timeout(value = 30)
  void evaluateFactAccuracyForGravityQuestion() {
    // given
    String question = "Who discovered the law of universal gravitation?";
    // when
    String aiResponse = chatController.chat(question);
    // then
    EvaluationRequest evaluationRequest = new EvaluationRequest(question, aiResponse);
    EvaluationResponse evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);

    Assertions.assertAll(
        () -> assertThat(aiResponse).isNotBlank(),
        () -> assertThat(evaluationResponse.isPass()).withFailMessage("""
                ================================================
                The answer was not considered factually correct.
                Question: "%s"
                Response: "%s"
                ================================================
                """, question, aiResponse)
            .isTrue()
    );
  }

  @Test
  @DisplayName("Should correct evaluate factual response based on HR policy context (RAG scenario)")
  @Timeout(value = 30)
  void evaluateHrPolicyAnswerWithRagContext() throws IOException {
    // given
    String question = "How many paid leave do employees get annually?";
    // when
    String aiResponse = promptStuffingController.promptStuffing(question);
    // then
    String retrievedContext = hrPolicyTemplate.getContentAsString(StandardCharsets.UTF_8);
    EvaluationRequest evaluationRequest = new EvaluationRequest(question,
        List.of(new Document(retrievedContext)), aiResponse);
    EvaluationResponse evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);

    Assertions.assertAll(
        () -> assertThat(aiResponse).isNotBlank(),
        () -> assertThat(evaluationResponse.isPass()).withFailMessage("""
                ================================================
                The response was not considered factually accurate.
                Question: "%s"
                Response: "%s"
                Context: "%s"
                ================================================
                """, question, aiResponse, retrievedContext)
            .isTrue()
    );
  }
}