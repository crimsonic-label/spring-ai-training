package pl.atd.mcpserverremote.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.atd.mcpserverremote.entity.HelpDeskTicket;

@Repository
public interface HelpDeskTicketRepository extends JpaRepository<HelpDeskTicket, Long> {
  List<HelpDeskTicket> findByUsername(String username);
}
