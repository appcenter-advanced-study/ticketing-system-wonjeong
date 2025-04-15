package com.appcenter.ticketing.ticket.repository;

import com.appcenter.ticketing.ticket.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
