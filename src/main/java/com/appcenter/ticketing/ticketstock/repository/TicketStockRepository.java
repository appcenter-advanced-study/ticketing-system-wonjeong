package com.appcenter.ticketing.ticketstock.repository;

import com.appcenter.ticketing.ticket.domain.Ticket;
import com.appcenter.ticketing.ticketstock.domain.TicketStock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface TicketStockRepository extends JpaRepository<TicketStock, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    TicketStock findByTicket(Ticket ticket);
}
