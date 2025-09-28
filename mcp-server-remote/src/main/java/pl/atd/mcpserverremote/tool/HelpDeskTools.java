package pl.atd.mcpserverremote.tool;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import pl.atd.mcpserverremote.entity.HelpDeskTicket;
import pl.atd.mcpserverremote.model.TicketRequest;
import pl.atd.mcpserverremote.service.HelpDeskTicketService;

@Component
@RequiredArgsConstructor
@Slf4j
public class HelpDeskTools {

  private final HelpDeskTicketService service;

  @Tool(name = "createTicket", description = "Create a Support ticket")
  public String createTicket(
      @ToolParam(description = "Details to create a Support ticket") TicketRequest ticketRequest) {

    HelpDeskTicket savedTicket = service.createTicket(ticketRequest);
    return "Ticket #" + savedTicket.getId() + " created successfully for user "
        + savedTicket.getUsername();
  }

  @Tool(name = "getTicketStatus", description = "Fetch the status of the tickets based on a given username")
  public List<HelpDeskTicket> getTicketStatus(
      @ToolParam(description = "Username to fetch the stats of the help desk tickets") String username) {

    List<HelpDeskTicket> ticketsByUsername = service.getTicketsByUsername(username);
    return ticketsByUsername;
  }
}
