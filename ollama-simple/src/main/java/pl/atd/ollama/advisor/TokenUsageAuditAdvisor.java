package pl.atd.ollama.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;

@Slf4j
public class TokenUsageAuditAdvisor implements CallAdvisor {

  @Override
  public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest,
      CallAdvisorChain callAdvisorChain) {
    ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
    ChatResponse chatResponse = chatClientResponse.chatResponse();
    if (chatResponse.getMetadata() != null) {
      Usage usage = chatResponse.getMetadata().getUsage();
      log.info("Prompt tokens {}, completion tokens: {}", usage.getTotalTokens(), usage.getCompletionTokens());
    }
    return chatClientResponse;

  }

  @Override
  public String getName() {
    return "TokenUsageAuditAdvisor";
  }

  @Override
  public int getOrder() {
    return 1;
  }
}
