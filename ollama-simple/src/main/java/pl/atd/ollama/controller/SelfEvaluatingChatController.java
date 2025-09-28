package pl.atd.ollama.controller;

import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.atd.ollama.exception.InvalidAnswerException;

/**
 * Will return response if evaluation says - the answer is proper
 */
@RestController
@RequestMapping("/api")
public class SelfEvaluatingChatController {

  private final ChatClient chatClient;
  private FactCheckingEvaluator factCheckingEvaluator;

  public SelfEvaluatingChatController(ChatClient.Builder builder) {
    this.chatClient = builder.defaultAdvisors(new SimpleLoggerAdvisor()).build();
    this.factCheckingEvaluator = new FactCheckingEvaluator(builder);
  }

  @GetMapping("/evaluate/chat")
  public String chat(@RequestParam("message") String message) {
    String response = chatClient.prompt(message).call().content();
    validateAnswer(message, response);
    return response;
  }

  private  void validateAnswer(String message, String answer) {
    EvaluationRequest evaluationRequest = new EvaluationRequest(message, List.of(), answer);
    EvaluationResponse evaluationResponse = factCheckingEvaluator.evaluate(evaluationRequest);

    // not working - FactCheckingEvaluator checks if documents contain the answer
    if(!evaluationResponse.isPass()) {
      throw new InvalidAnswerException(message, answer);
    }
  }
}
