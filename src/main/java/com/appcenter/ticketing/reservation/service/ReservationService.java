package com.appcenter.ticketing.reservation.service;

import com.appcenter.ticketing.reservation.domain.Reservation;
import com.appcenter.ticketing.reservation.dto.ReservationResponse;
import com.appcenter.ticketing.reservation.repository.ReservationRepository;
import com.appcenter.ticketing.ticket.domain.Ticket;
import com.appcenter.ticketing.ticket.dto.TicketResponse;
import com.appcenter.ticketing.ticket.repository.TicketRepository;
import com.appcenter.ticketing.ticketstock.domain.TicketStock;
import com.appcenter.ticketing.ticketstock.repository.TicketStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;
    private final TicketStockRepository ticketStockRepository;

    @Transactional
    public void createReservation(String username, Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow();
        Reservation reservation = new Reservation(username, ticket);
        TicketStock ticketStock = ticketStockRepository.findByTicket(ticket);
        ticketStock.decreaseQuantity();
        ticketStockRepository.save(ticketStock);
        reservationRepository.save(reservation);
    }

    @Transactional(readOnly = true)
    public ReservationResponse findById(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow();
        return new ReservationResponse(reservation.getId(), reservation.getUsername(), new TicketResponse(reservation.getTicket().getId(), reservation.getTicket().getName()));
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream().map(reservation -> new ReservationResponse(reservation.getId(), reservation.getUsername(), new TicketResponse(reservation.getTicket().getId(), reservation.getTicket().getName()))).toList();
    }

    @Transactional
    public ReservationResponse updateReservation(Long id, String username, Long ticketId) {
        // 예약건 조회
        Reservation foundReservation = reservationRepository.findById(id).orElseThrow();

        if (ticketId != null) {
            // 현 티켓 조회
            Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
            // 예약건과 원래 관계 되어있었던 티켓정보를 가져옴
            Ticket oldTicket = foundReservation.getTicket();
            // 구 티켓 != 현 티켓
            if (ticket != null && !oldTicket.equals(ticket)) {
                TicketStock ticketStock = ticketStockRepository.findByTicket(oldTicket);
                // 티켓 반환 (구 티켓 재고에서 +1을 해줌)
                ticketStock.increaseQuantity();
                ticketStockRepository.save(ticketStock);
            }

            // 현 티켓과 관계를 맺는다.
            foundReservation.updateReservation(username, ticket);
        }

        foundReservation.updateReservation(username, null);
        reservationRepository.save(foundReservation);

        return new ReservationResponse(foundReservation.getId(), foundReservation.getUsername(), new TicketResponse(foundReservation.getTicket().getId(), foundReservation.getTicket().getName()));
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
