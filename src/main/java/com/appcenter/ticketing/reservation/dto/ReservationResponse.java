package com.appcenter.ticketing.reservation.dto;

import com.appcenter.ticketing.ticket.dto.TicketResponse;

public record ReservationResponse(
        Long id,
        String username,
        TicketResponse ticket
) {
}
