package pl.atd.ollama;

import java.util.Collections;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.evaluation.Evaluator;

public class FactCheckingFixedEvaluator implements Evaluator {

  private static final String DEFAULT_EVALUATION_PROMPT_TEXT = """
				Evaluate whether or not the following claim is supported by the provided document.
				Respond with "yes" if the claim is supported, or "no" if it is not.
				Document: \\n {document}\\n
				Claim: \\n {claim}
			""";

  private final ChatClient.Builder chatClientBuilder;

  public FactCheckingFixedEvaluator(ChatClient.Builder chatClientBuilder) {
    this.chatClientBuilder = chatClientBuilder;
  }

  @Override
  public EvaluationResponse evaluate(EvaluationRequest evaluationRequest) {
    var response = evaluationRequest.getResponseContent();
    var context = doGetSupportingData(evaluationRequest);

    String evaluationResponse = this.chatClientBuilder.build()
        .prompt()
        .user(userSpec -> userSpec.text(DEFAULT_EVALUATION_PROMPT_TEXT).param("document", context).param("claim", response))
        .call()
        .content();

    boolean passing = evaluationResponse.toLowerCase().trim().startsWith("yes");
    return new EvaluationResponse(passing, "", Collections.emptyMap());
  }
}
