package com.appcenter.ticketing.ticketstock.repository;

import com.appcenter.ticketing.ticket.domain.Ticket;
import com.appcenter.ticketing.ticketstock.domain.TicketStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketStockRepository extends JpaRepository<TicketStock, Long> {
    TicketStock findByTicket(Ticket ticket);
}
