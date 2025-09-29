package pl.atd.voicetotext.model;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

public record TranscriptionRequest(MultipartFile audioFile, @Nullable String context) {

}
