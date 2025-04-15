package com.appcenter.ticketing.ticket.controller;

import com.appcenter.ticketing.ticket.dto.TicketResponse;
import com.appcenter.ticketing.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ticket")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<Void> createTicket(@RequestParam String name) {
        ticketService.createTicket(name);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<TicketResponse> getTicket(@RequestParam Long id) {
        TicketResponse result = ticketService.findById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TicketResponse>> getAllTicket() {
        List<TicketResponse> result = ticketService.findAll();
        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<TicketResponse> updateTicket(@RequestParam Long id, @RequestParam String name) {
        TicketResponse result = ticketService.updateTicket(id, name);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTicket(@RequestParam Long id) {
        ticketService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
