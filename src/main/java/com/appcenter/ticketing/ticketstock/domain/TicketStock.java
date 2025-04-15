package com.appcenter.ticketing.ticketstock.domain;

import com.appcenter.ticketing.ticket.domain.Ticket;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public TicketStock(Integer quantity, Ticket ticket) {
        this.quantity = quantity;
        this.ticket = ticket;
    }

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void decreaseQuantity() {
        if (this.quantity <= 0) {
            throw new IllegalStateException("No more tickets available");
        } else {
            this.quantity--;
        }
    }

    public void increaseQuantity() {
        this.quantity++;
    }
}
