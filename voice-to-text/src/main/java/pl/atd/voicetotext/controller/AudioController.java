package pl.atd.voicetotext.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.api.OpenAiAudioApi.TranscriptResponseFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AudioController {

  private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;

  @GetMapping("/transcribe")
  public String transcribe(@Value("classpath:audio/SpringAi.mp3") Resource audioFile) {
    return openAiAudioTranscriptionModel.call(audioFile);
  }
  @GetMapping("/transcribe-options")
  public String transcribeWithOptions(@Value("classpath:audio/SpringAi.mp3") Resource audioFile) {
    return openAiAudioTranscriptionModel.call(new AudioTranscriptionPrompt(audioFile,
        OpenAiAudioTranscriptionOptions.builder()
            .prompt("Talking about Spring AI")
            .language("en")
            .temperature(0.5f)
            // for subtitles
            .responseFormat(TranscriptResponseFormat.VTT)
            .build()))
        .getResult()
        .getOutput();
  }
}
