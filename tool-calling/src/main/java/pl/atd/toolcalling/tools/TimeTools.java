package pl.atd.toolcalling.tools;

import java.time.LocalTime;
import java.time.ZoneId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TimeTools {

  @Tool(name="getCurrentLocalTime", description = "Get the current time in the user's timezone")
  public String getCurrentLocalTime() {
    log.info("Returning the current tome in the user's timezone");
    return LocalTime.now().toString();
  }

  @Tool(name="getCurrentTime", description = "Get the current time in the specified time zone")
  public String getCurrentTime(@ToolParam(description = "Value representing the time zone") String timeZone) {
    log.info("Returning the current tome in the timezone {}", timeZone);
    return LocalTime.now(ZoneId.of(timeZone)).toString();
  }
}
