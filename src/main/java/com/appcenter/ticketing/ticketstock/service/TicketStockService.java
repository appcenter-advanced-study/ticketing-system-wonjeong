package com.appcenter.ticketing.ticketstock.service;

import com.appcenter.ticketing.ticket.domain.Ticket;
import com.appcenter.ticketing.ticket.dto.TicketResponse;
import com.appcenter.ticketing.ticket.repository.TicketRepository;
import com.appcenter.ticketing.ticketstock.domain.TicketStock;
import com.appcenter.ticketing.ticketstock.dto.TicketStockResponse;
import com.appcenter.ticketing.ticketstock.repository.TicketStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketStockService {
    private final TicketStockRepository ticketStockRepository;
    private final TicketRepository ticketRepository;

    public Integer findQuantityByTicketId(Long ticketStockId) {
        TicketStock ticketStock = ticketStockRepository.findById(ticketStockId).orElseThrow();
        return ticketStock.getQuantity();
    }

    public void setStockQuantity(Long ticketId, Integer quantity) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        TicketStock ticketStock = new TicketStock(quantity, ticket);
        ticketStockRepository.save(ticketStock);
    }

    public TicketStockResponse updateStockQuantity(Long ticketId, Integer quantity) {
        TicketStock ticketStock = ticketStockRepository.findById(ticketId).orElseThrow();
        ticketStock.updateQuantity(quantity);
        TicketStock savedTicketStock = ticketStockRepository.save(ticketStock);
        return new TicketStockResponse(savedTicketStock.getId(), savedTicketStock.getQuantity(), new TicketResponse(savedTicketStock.getTicket().getId(), savedTicketStock.getTicket().getName()));
    }
}
