package com.appcenter.ticketing.ticketstock.dto;

import com.appcenter.ticketing.ticket.dto.TicketResponse;

public record TicketStockResponse(
        Long id,
        Integer quantity,
        TicketResponse ticket
) {
}
