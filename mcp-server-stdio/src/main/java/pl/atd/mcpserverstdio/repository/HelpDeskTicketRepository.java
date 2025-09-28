package pl.atd.mcpserverstdio.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.atd.mcpserverstdio.entity.HelpDeskTicket;

@Repository
public interface HelpDeskTicketRepository extends JpaRepository<HelpDeskTicket, Long> {
  List<HelpDeskTicket> findByUsername(String username);
}
