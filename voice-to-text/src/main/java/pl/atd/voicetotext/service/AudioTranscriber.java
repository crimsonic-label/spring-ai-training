package pl.atd.voicetotext.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.stereotype.Service;
import pl.atd.voicetotext.model.TranscriptionRequest;
import pl.atd.voicetotext.model.TranscriptionResponse;

@Service
@RequiredArgsConstructor
public class AudioTranscriber {
  private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;

  public TranscriptionResponse transcribe(TranscriptionRequest transcriptionRequest) {
    AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(
        transcriptionRequest.audioFile().getResource(),
        OpenAiAudioTranscriptionOptions
            .builder()
            .prompt(transcriptionRequest.context())
            .build()
    );
    AudioTranscriptionResponse response = openAiAudioTranscriptionModel.call(prompt);
    return new TranscriptionResponse(response.getResult().getOutput());
  }
}
