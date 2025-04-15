package com.appcenter.ticketing.ticketstock.controller;

import com.appcenter.ticketing.ticketstock.dto.TicketStockResponse;
import com.appcenter.ticketing.ticketstock.service.TicketStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ticket-stock")
@RequiredArgsConstructor
public class TicketStockController {
    private final TicketStockService ticketStockService;

    @PostMapping
    public ResponseEntity<Void> setStockQuantity(@RequestParam Long ticketId, @RequestParam Integer quantity) {
        ticketStockService.setStockQuantity(ticketId, quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Integer> getStockQuantity(@RequestParam Long ticketStockId) {
        Integer result = ticketStockService.findQuantityByTicketId(ticketStockId);
        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<TicketStockResponse> updateStockQuantity(@RequestParam Long ticketStockId, @RequestParam int quantity) {
        TicketStockResponse result = ticketStockService.updateStockQuantity(ticketStockId, quantity);
        return ResponseEntity.ok(result);
    }


}
