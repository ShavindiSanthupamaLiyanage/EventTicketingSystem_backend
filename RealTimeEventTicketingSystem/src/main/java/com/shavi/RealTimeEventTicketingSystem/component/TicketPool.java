package com.shavi.RealTimeEventTicketingSystem.component;

import com.shavi.RealTimeEventTicketingSystem.entity.Ticket;
import com.shavi.RealTimeEventTicketingSystem.enums.TicketStatus;
import com.shavi.RealTimeEventTicketingSystem.repository.TicketRepository;
import com.shavi.RealTimeEventTicketingSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TicketPool {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    public synchronized void addTickets(int numberOfTickets, Long eventId) {
        for (int i = 0; i < numberOfTickets; i++) {
            Ticket ticket = new Ticket();
            ticket.setEventId(eventId);
            ticket.setStatus(TicketStatus.AVAILABLE);
            ticketRepository.save(ticket);
        }
        System.out.println(numberOfTickets + " tickets added to the pool.");
    }

    public synchronized void purchaseTicket(Long eventId, Integer userId, int quantity) {
        boolean userExists = userRepository.existsById(userId);
        if (!userExists) {
            throw new IllegalArgumentException("User does not exist.");
        }

        Pageable pageable = PageRequest.of(0, quantity); // Create Pageable for quantity
        List<Ticket> availableTickets = ticketRepository.findTopNAvailableTickets(eventId, pageable);

        if (availableTickets.size() < quantity) {
            throw new IllegalStateException("Not enough tickets available.");
        }

        for (Ticket ticket : availableTickets) {
            ticket.setStatus(TicketStatus.SOLD);
            ticket.setUserId(userId);
            ticketRepository.save(ticket);
        }
        System.out.println(quantity + " tickets purchased.");
    }
}

