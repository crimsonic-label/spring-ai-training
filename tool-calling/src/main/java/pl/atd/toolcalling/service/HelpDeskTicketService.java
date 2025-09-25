package pl.atd.toolcalling.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.atd.toolcalling.entity.HelpDeskTicket;
import pl.atd.toolcalling.model.TicketRequest;
import pl.atd.toolcalling.repository.HelpDeskTicketRepository;

@Service
@RequiredArgsConstructor
public class HelpDeskTicketService {

  private final HelpDeskTicketRepository helpDeskTicketRepository;

  public HelpDeskTicket createTicket(TicketRequest ticketRequest, String username) {
    HelpDeskTicket ticket = HelpDeskTicket.builder()
        .issue(ticketRequest.issue())
        .username(username)
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
