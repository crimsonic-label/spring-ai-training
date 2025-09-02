package pl.atd.subtitles;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SubtitleLineProcessor {

  private static final Pattern timingPattern = Pattern.compile(
      "\\d{2}:\\d{2}:\\d{2},\\d{3} --> \\d{2}:\\d{2}:\\d{2},\\d{3}");

  private final Translator translator;

  public void translate(String sourceFileName, String resultFileName) throws IOException {

    AtomicLong counter = new AtomicLong(0);
    StringBuilder subtitle = new StringBuilder();

    try (Stream<String> lines = java.nio.file.Files.lines(Paths.get(sourceFileName));
        BufferedWriter writer = new BufferedWriter(new FileWriter(resultFileName, false))
    ) {
      lines.forEach(line -> {
        counter.addAndGet(1);

        if (Pattern.matches("\\d+", line)) {
          // first line - text number,
          writeLine(line, writer);
          log.info(line);

        } else if (timingPattern.matcher(line).find()) {
          // the second line - timing
          writeLine(line, writer);

        } else if (line.isEmpty()) {
          // the last empty one

          // write all collected subtile lines and clear the builder
          String translated = translator.translate(subtitle.toString());
          writeLine("{b}" + translated + "{/b}", writer);
          log.info(subtitle.toString().trim() + " -> " + translated);
          subtitle.setLength(0);

          // add new line
          writeLine(line, writer);
        } else {
          // line with a single subtitle
          writeLine(line, writer);

          if(!subtitle.isEmpty()) {
            subtitle.append("\n");
          }
          subtitle.append(line);
        }
      });

      // last not finished subtitle
      if(!subtitle.isEmpty()) {
        String translated = translator.translate(subtitle.toString());
        writeLine("{b}" + translated + "{/b}", writer);
        log.info(subtitle.toString().trim() + " -> " + translated);
      }
      log.info("\nProcessing finished. Read {} lines", counter.get());
    }
  }

  private void writeLine(String lineContent, BufferedWriter writer) {
    try {
      writer.write(lineContent);
      writer.newLine();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
