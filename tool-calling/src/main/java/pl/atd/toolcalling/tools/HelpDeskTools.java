package pl.atd.toolcalling.tools;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import pl.atd.toolcalling.entity.HelpDeskTicket;
import pl.atd.toolcalling.model.TicketRequest;
import pl.atd.toolcalling.service.HelpDeskTicketService;

@Component
@RequiredArgsConstructor
@Slf4j
public class HelpDeskTools {

  private final HelpDeskTicketService service;

  @Tool(name = "createTicket", description = "Create a Support ticket")
  public String createTicket(
      @ToolParam(description = "Details to create a Support ticket") TicketRequest ticketRequest,
      ToolContext toolContext) {

    String username = (String) toolContext.getContext().get("username");
    log.info("Calling createTicket tools for user {} with ticket request: {}", username, ticketRequest);

    HelpDeskTicket savedTicket = service.createTicket(ticketRequest, username);
    return "Ticket #" + savedTicket.getId() + " created successfully for user " + savedTicket.getUsername();
  }

  @Tool(name="getTicketStatus", description = "Fetch the status of the tickets based on a given username")
  public List<HelpDeskTicket> getTicketStatus(ToolContext toolContext) {
    String username = (String) toolContext.getContext().get("username");
    log.info("Calling getTicketStatus tools for user {}", username);

    List<HelpDeskTicket> ticketsByUsername = service.getTicketsByUsername(username);
    log.info("Found {} tickets for user {}", ticketsByUsername.size(), username);

    return ticketsByUsername;
  }
}
