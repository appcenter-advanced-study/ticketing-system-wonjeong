package com.appcenter.ticketing.reservation.domain;

import com.appcenter.ticketing.ticket.domain.Ticket;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String username;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public Reservation(String username, Ticket ticket) {
        this.username = username;
        this.ticket = ticket;
    }

    public void updateReservation(String username, Ticket ticket) {
        this.ticket = ticket != null ? ticket : this.ticket;
        this.username = username != null ? username : this.username;
    }
}
