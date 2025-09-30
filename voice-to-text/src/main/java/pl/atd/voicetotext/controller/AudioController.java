package pl.atd.voicetotext.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.api.OpenAiAudioApi.SpeechRequest.AudioResponseFormat;
import org.springframework.ai.openai.api.OpenAiAudioApi.SpeechRequest.Voice;
import org.springframework.ai.openai.api.OpenAiAudioApi.TranscriptResponseFormat;
import org.springframework.ai.openai.audio.speech.SpeechModel;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
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
  private final SpeechModel speechModel;
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

  @GetMapping("/speech")
  public String speech(@RequestParam("message") String message) throws IOException {
    Path audioPath = Paths.get("output.mp3");
    Files.write(audioPath, speechModel.call(message));
    return "MP3 saved successfully to " + audioPath.toAbsolutePath();
  }

  @GetMapping("/speech-options")
  public String speechWithOptions(@RequestParam("message") String message) throws IOException {
    Path audioPath = Paths.get("output.mp3");
    Files.write(audioPath, speechModel.call(
        new SpeechPrompt(message,
            OpenAiAudioSpeechOptions.builder()
                .voice(Voice.NOVA)
                .speed(2.0f)
                .responseFormat(AudioResponseFormat.FLAC)
                .build()))
        .getResult().getOutput());
    return "MP3 saved successfully to " + audioPath.toAbsolutePath();
  }
}
