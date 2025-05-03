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

        try {
            Thread.sleep(500); // 락 보유 시간을 인위적으로 증가
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

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
                TicketStock oldTicketStock = ticketStockRepository.findByTicket(oldTicket);
                TicketStock newTicketStock = ticketStockRepository.findByTicket(ticket);
                // 원래 티켓 반환 (구 티켓 재고에서 +1을 해줌)
                oldTicketStock.increaseQuantity();
                // 새로운 티켓 판매 (새 티켓 재고에서 -1을 해줌)
                newTicketStock.decreaseQuantity();
                ticketStockRepository.save(oldTicketStock);
                ticketStockRepository.save(newTicketStock);
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
        Reservation reservation = reservationRepository.findById(id).orElseThrow();
        Ticket ticket = reservation.getTicket();
        TicketStock ticketStock = ticketStockRepository.findByTicket(ticket);
        // 예약 삭제 시 티켓 반환
        ticketStock.increaseQuantity();
        ticketStockRepository.save(ticketStock);
        reservationRepository.delete(reservation);
    }
}
