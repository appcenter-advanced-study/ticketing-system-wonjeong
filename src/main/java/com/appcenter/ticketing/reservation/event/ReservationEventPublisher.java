package com.appcenter.ticketing.reservation.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationEventPublisher {
    private final KafkaTemplate<String, ReservationEvent> kafkaTemplate;
    private static final String TOPIC = "reservation-event";

    public void publishReservationCreatedEvent(ReservationEvent reservationEvent) {
        kafkaTemplate.send(TOPIC, reservationEvent);
    }
}
