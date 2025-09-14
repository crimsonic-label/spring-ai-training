package pl.atd.subtitles;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.util.Assert;

@Slf4j
public class SubtitlesChatMemory implements ChatMemory {

  private final MessageWindowChatMemory chatMemory;

  public SubtitlesChatMemory(ChatMemoryRepository chatMemoryRepository, int maxMessages) {
    chatMemory = MessageWindowChatMemory.builder()
        .chatMemoryRepository(chatMemoryRepository)
        .maxMessages(maxMessages)
        .build();
  }

  @Override
  public void add(String conversationId, List<Message> messages) {
    // add only user messages
    chatMemory.add(conversationId, messages.stream()
        .filter(m -> m.getMessageType() == MessageType.USER)
        .toList());
  }

  @Override
  public void add(String conversationId, Message message) {
    Assert.hasText(conversationId, "conversationId cannot be null or empty");
    Assert.notNull(message, "message cannot be null");
    this.add(conversationId, List.of(message));
  }

  @Override
  public List<Message> get(String conversationId) {
    return chatMemory.get(conversationId);
  }

  @Override
  public void clear(String conversationId) {
    chatMemory.clear(conversationId);
  }
}
