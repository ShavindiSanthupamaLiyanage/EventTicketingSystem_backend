package com.shavi.RealTimeEventTicketingSystem.component;

import com.shavi.RealTimeEventTicketingSystem.entity.Event;
import com.shavi.RealTimeEventTicketingSystem.entity.Ticket;
import com.shavi.RealTimeEventTicketingSystem.enums.TicketStatus;
import com.shavi.RealTimeEventTicketingSystem.repository.EventRepository;
import com.shavi.RealTimeEventTicketingSystem.repository.TicketRepository;
import com.shavi.RealTimeEventTicketingSystem.repository.UserRepository;
import com.shavi.RealTimeEventTicketingSystem.service.EventService;
import com.shavi.RealTimeEventTicketingSystem.service.SystemConfigurationService;
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

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @Autowired
    private EventRepository eventRepository;

    public synchronized void addTickets(int numberOfTickets, Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found."));

        // Check if the system configuration is running
        if (systemConfigurationService.getRunningConfiguration() == null) {
            throw new IllegalStateException("System configuration is not running. Tickets cannot be added.");
        }

        // Count the current number of tickets in the pool for this event
        long currentTickets = ticketRepository.countByEventIdAndStatus(eventId, TicketStatus.AVAILABLE);

        // Ensure the new tickets do not exceed the total limit for the event
        if (currentTickets + numberOfTickets > event.getNoOfTickets()) {
            throw new IllegalStateException("Adding these tickets exceeds the event's total ticket limit.");
        }

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

