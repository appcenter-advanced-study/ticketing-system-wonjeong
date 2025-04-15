package com.appcenter.ticketing;

import com.appcenter.ticketing.reservation.repository.ReservationRepository;
import com.appcenter.ticketing.reservation.service.ReservationService;
import com.appcenter.ticketing.ticket.domain.Ticket;
import com.appcenter.ticketing.ticket.repository.TicketRepository;
import com.appcenter.ticketing.ticketstock.domain.TicketStock;
import com.appcenter.ticketing.ticketstock.repository.TicketStockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ReservationServiceConcurrencyTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketStockRepository ticketStockRepository;

    private Long ticketId;

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        // 기존 데이터 정리
        reservationRepository.deleteAll();
        ticketStockRepository.deleteAll();
        ticketRepository.deleteAll();

        // 테스트용 티켓 생성
        Ticket ticket = new Ticket("콘서트 티켓");
        ticketRepository.save(ticket);

        // 재고 1개 설정
        TicketStock ticketStock = new TicketStock(1, ticket);
        ticketStockRepository.save(ticketStock);

        ticketId = ticket.getId();
    }

    @Test
    void testConcurrentReservation() throws InterruptedException {
        int numberOfThreads = 5; // 동시에 5개 요청 생성
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        List<Exception> exceptions = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            final String username = "user" + i;

            executorService.submit(() -> {
                try {
                    reservationService.createReservation(username, ticketId);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    synchronized (exceptions) {
                        exceptions.add(e);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 완료될 때까지 대기
        executorService.shutdown();

        // 결과 검증
        TicketStock ticketStock = ticketStockRepository.findByTicket(
                ticketRepository.findById(ticketId).orElseThrow());

        System.out.println("성공한 예약 수: " + successCount.get());
        System.out.println("남은 재고: " + ticketStock.getQuantity());
        System.out.println("발생한 예외 수: " + exceptions.size());

        // 동시성 보호가 없으면 여러 예약이 성공하고 재고가 음수가 됨
        if (successCount.get() > 1) {
            System.out.println("이중 예약 문제 발생: " + successCount.get() + "개의 예약이 성공했습니다.");
            assertTrue(ticketStock.getQuantity() < 0, "재고가 음수가 되어야 합니다");
        } else {
            System.out.println("정상 동작: 하나의 예약만 성공했습니다.");
            assertEquals(0, ticketStock.getQuantity(), "재고가 0이어야 합니다");
            assertEquals(numberOfThreads - 1, exceptions.size(), "나머지 요청은 예외가 발생해야 합니다");
        }
    }
}