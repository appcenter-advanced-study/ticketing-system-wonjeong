package com.appcenter.ticketing.reservation.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
public class ReservationEvent {
    private String username;
    private Long ticketId;
    private LocalDateTime createdAt;
}
