package pl.atd.subtitles;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class Translator {

  public static final String SUBTITLES_TRANSLATOR_SYSTEM_MESSAGE = """
      You are translating movie subtitles in English to Polish.
      Please answer with only the best translation without any comment.
      Please preserve new lines""";

  private final ChatClient chatClient;

  public String translate(String text) {
    return Optional.ofNullable(chatClient.prompt()
            .user(text.trim())
            .system(SUBTITLES_TRANSLATOR_SYSTEM_MESSAGE)
            .call().content())
        .map(String::trim)
        .orElse(null);
  }
}
