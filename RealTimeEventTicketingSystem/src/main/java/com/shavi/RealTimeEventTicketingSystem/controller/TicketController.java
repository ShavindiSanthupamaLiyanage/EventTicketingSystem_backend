package com.shavi.RealTimeEventTicketingSystem.controller;

import com.shavi.RealTimeEventTicketingSystem.configurations.LoggerConfiguration;
import com.shavi.RealTimeEventTicketingSystem.entity.Ticket;
import com.shavi.RealTimeEventTicketingSystem.service.EventService;
import com.shavi.RealTimeEventTicketingSystem.service.TicketService;
import com.shavi.RealTimeEventTicketingSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tickets")
public class TicketController extends LoggerConfiguration {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseTickets(@RequestParam Long eventId, @RequestParam Integer userId, @RequestParam int quantity) {
        try {
            ticketService.purchaseTickets(eventId, userId, quantity);
            logger.info("Tickets purchased successfully for event ID: {} by user ID: {}", eventId, userId);
            return ResponseEntity.ok("Tickets purchased successfully.");
        } catch (IllegalStateException e) {
            logger.error("Error purchasing tickets for event ID: {} by user ID: {}: {}", eventId, userId, e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (InterruptedException e) {
            logger.error("Purchase process interrupted for event ID: {} by user ID: {}", eventId, userId);
            Thread.currentThread().interrupt();
            return ResponseEntity.status(500).body("Purchase process interrupted.");
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<Ticket>> getAvailableTickets(
            @RequestParam Long eventId,
            @RequestParam int quantity) {
        logger.info("Received request to fetch {} available tickets for event ID: {}", quantity, eventId);
        List<Ticket> tickets = ticketService.getAvailableTickets(eventId, quantity);

        if (tickets.isEmpty()) {
            logger.warn("No available tickets found for event ID: {}", eventId);
            return ResponseEntity.status(404).body(tickets);
        }

        logger.info("Found {} available tickets for event ID: {}", tickets.size(), eventId);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/user/{userId}/ticket-counts")
    public ResponseEntity<Map<Long, Long>> getTicketCountsByUserId(@PathVariable Integer userId) {
        logger.info("Fetching ticket counts for user ID: {}", userId);

        Map<Long, Long> ticketCounts = ticketService.getTicketCountsByUserId(userId);
        logger.info("Found ticket counts for user ID: {}: {}", userId, ticketCounts);
        return ResponseEntity.ok(ticketCounts);
    }

}
