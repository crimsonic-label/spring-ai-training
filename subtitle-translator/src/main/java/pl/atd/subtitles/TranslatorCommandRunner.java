package pl.atd.subtitles;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TranslatorCommandRunner implements CommandLineRunner {

  private final SubtitleLineProcessor subtitleLineProcessor;

  @Override
  public void run(String... args) throws Exception {
    if (args.length != 2) {
      log.error("pass two arguments:");
      log.error("\t1. source file to be translated");
      log.error("\t2. target file with translations");
      return;
    }
    String sourceFileName = args[0];
    String resultFileName = args[1];
    log.info("Translating {} file to {}", sourceFileName, resultFileName);

    subtitleLineProcessor.translate(sourceFileName, resultFileName);
  }
}
