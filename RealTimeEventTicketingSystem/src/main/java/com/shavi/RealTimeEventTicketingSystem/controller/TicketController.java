package com.shavi.RealTimeEventTicketingSystem.controller;

import com.shavi.RealTimeEventTicketingSystem.entity.Ticket;
import com.shavi.RealTimeEventTicketingSystem.service.EventService;
import com.shavi.RealTimeEventTicketingSystem.service.TicketService;
import com.shavi.RealTimeEventTicketingSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseTickets(@RequestParam Long eventId, @RequestParam Integer userId, @RequestParam int quantity) {
        try {
            ticketService.purchaseTickets(eventId, userId, quantity);
            return ResponseEntity.ok("Tickets purchased successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResponseEntity.status(500).body("Purchase process interrupted.");
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<Ticket>> getAvailableTickets(
            @RequestParam Long eventId,
            @RequestParam int quantity) {
        List<Ticket> tickets = ticketService.getAvailableTickets(eventId, quantity);
        return ResponseEntity.ok(tickets);
    }
}
