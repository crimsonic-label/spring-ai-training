package pl.atd.subtitles;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Translator {

  private static final String PROMPT_TEMPLATE = "Translate to Polish {textToTranslate}, answer with only the best translation without any comments";

  private final ChatClient chatClient;

  public Translator(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  public String translate(String text) {
    PromptTemplate promptTemplate = new PromptTemplate(PROMPT_TEMPLATE);
    promptTemplate.add("textToTranslate", text.trim());
    return chatClient.prompt(promptTemplate.create()).call().content().trim();
  }
}
