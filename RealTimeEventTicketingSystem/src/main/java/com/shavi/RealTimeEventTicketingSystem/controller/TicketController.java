package com.shavi.RealTimeEventTicketingSystem.controller;

import com.shavi.RealTimeEventTicketingSystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

//    // Existing endpoint for buying tickets
//    @PostMapping("/buyTickets")
//    public ResponseEntity<String> buyTicket(
//            @RequestParam Long eventId,
//            @RequestParam Long customerId,
//            @RequestParam int quantity) {
//        String response = ticketService.buyTicket(eventId, customerId, quantity);
//        if (response.contains("successful")) {
//            return ResponseEntity.ok(response);
//        } else {
//            return ResponseEntity.badRequest().body(response);
//        }
//    }

    // Simplified endpoint for CLI to buy tickets (assumes a default customer)
    @PostMapping("/buy")
    public ResponseEntity<String> buyTicketCLI(@RequestParam Long eventId, @RequestParam int quantity) {
        // Using a default customer ID for simplicity
        Long defaultCustomerId = 1L; // Replace with an actual ID in your database
        String response = ticketService.buyTicket(eventId, defaultCustomerId, quantity);
        if (response.contains("successful")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    // New endpoint to monitor ticket availability for an event
    @GetMapping("/availability")
    public ResponseEntity<Integer> checkAvailability(@RequestParam Long eventId) {
        try {
            int availableTickets = ticketService.getAvailableTickets(eventId);
            return ResponseEntity.ok(availableTickets);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(0);
        }
    }

    // New endpoint for vendors to add tickets
    @PostMapping("/addTickets")
    public ResponseEntity<String> addTickets(
            @RequestParam Long eventId,
            @RequestParam int quantity) {
        String response = ticketService.addTickets(eventId, quantity);
        if (response.contains("successful")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
