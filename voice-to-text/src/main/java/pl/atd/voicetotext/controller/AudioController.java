package pl.atd.voicetotext.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi.TranscriptResponseFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.atd.voicetotext.model.TranscriptionResponse;
import pl.atd.voicetotext.model.TranscriptionRequest;
import pl.atd.voicetotext.service.AudioTranscriber;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AudioController {

  private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;
  private final AudioTranscriber audioTranscriber;

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

  @PostMapping("/transcribe")
  ResponseEntity<TranscriptionResponse> fileTranscribe(
      @RequestParam("audioFile") MultipartFile audioFile,
      @RequestParam("context") String context
  ) {
    TranscriptionRequest transcriptionRequest = new TranscriptionRequest(audioFile, context);
    TranscriptionResponse response = audioTranscriber.transcribe(transcriptionRequest);
    return ResponseEntity.ok(response);
  }
}
