package com.appcenter.ticketing.reservation.repository;

import com.appcenter.ticketing.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
