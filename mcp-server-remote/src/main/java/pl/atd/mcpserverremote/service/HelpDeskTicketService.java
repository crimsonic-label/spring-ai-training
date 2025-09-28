package pl.atd.mcpserverremote.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.atd.mcpserverremote.entity.HelpDeskTicket;
import pl.atd.mcpserverremote.model.TicketRequest;
import pl.atd.mcpserverremote.repository.HelpDeskTicketRepository;

@Service
@RequiredArgsConstructor
public class HelpDeskTicketService {

  private final HelpDeskTicketRepository helpDeskTicketRepository;

  public HelpDeskTicket createTicket(TicketRequest ticketRequest) {
    HelpDeskTicket ticket = HelpDeskTicket.builder()
        .issue(ticketRequest.issue())
        .username(ticketRequest.username())
        .status("OPEN")
        .createdAt(LocalDateTime.now())
        .eta(LocalDateTime.now().plusDays(7))
        .build();
    return helpDeskTicketRepository.save(ticket);
  }

  public List<HelpDeskTicket> getTicketsByUsername(String username) {
    return helpDeskTicketRepository.findByUsername(username);
  }
}
