package com.shavi.RealTimeEventTicketingSystem.controller;

import com.shavi.RealTimeEventTicketingSystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
public class Ticket {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/buyTickets")
    public String buyTicket(
            @RequestParam Long eventId,
            @RequestParam Long customerId,
            @RequestParam int quantity) {
        return ticketService.buyTicket(eventId, customerId, quantity);
    }
}
