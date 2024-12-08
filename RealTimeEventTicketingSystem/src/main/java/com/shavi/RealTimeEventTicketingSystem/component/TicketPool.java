package com.shavi.RealTimeEventTicketingSystem.component;

import com.shavi.RealTimeEventTicketingSystem.configurations.LoggerConfiguration;
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
public class TicketPool extends LoggerConfiguration {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @Autowired
    private EventRepository eventRepository;

    public synchronized void addTickets(int numberOfTickets, Long eventId) {
        while (systemConfigurationService.getRunningConfiguration() == null) {
            try {
                logger.info("Waiting for system configuration to be ready...");
                Thread.sleep(1000);
                wait(); // Wait until the system configuration is running
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Thread interrupted while waiting for system configuration.", e);
                throw new IllegalStateException("Thread interrupted while waiting for system configuration.");
            }
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    logger.error("Event with ID {} not found.", eventId);
                    return new IllegalArgumentException("Event not found.");
                });

//        try {
//            Thread.sleep(1000); // Simulate delay after fetching event details
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }

        long currentTickets = ticketRepository.countByEventIdAndStatus(eventId, TicketStatus.AVAILABLE);

        if (currentTickets + numberOfTickets > event.getNoOfTickets()) {
            logger.error("Adding {} tickets exceeds the event's total ticket limit for event {}", numberOfTickets, eventId);
            throw new IllegalStateException("Adding these tickets exceeds the event's total ticket limit.");
        }

        for (int i = 0; i < numberOfTickets; i++) {
            Ticket ticket = new Ticket();
            ticket.setEventId(eventId);
            ticket.setStatus(TicketStatus.AVAILABLE);

            try {
                Thread.sleep(1000); // Simulate delay for each ticket save operation
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Thread interrupted while adding ticket for event {}", eventId, e);
                break; // Exit loop on interruption
            }

            ticketRepository.save(ticket);
            logger.info("{} tickets added to the pool for event {}", numberOfTickets, eventId);
        }
        logger.info("{} tickets added to the pool for event {}", numberOfTickets, eventId);
        notifyAll(); // Notify all threads that tickets have been added
    }

//    public synchronized void purchaseTicket(Long eventId, Integer userId, int quantity) {
//        boolean userExists = userRepository.existsById(userId);
//        if (!userExists) {
//            throw new IllegalArgumentException("User does not exist.");
//        }
//
//        Pageable pageable = PageRequest.of(0, quantity); // Create Pageable for quantity
//        List<Ticket> availableTickets = ticketRepository.findTopNAvailableTickets(eventId, pageable);
//
//        if (availableTickets.size() < quantity) {
//            throw new IllegalStateException("Not enough tickets available.");
//        }
//
//        for (Ticket ticket : availableTickets) {
//            ticket.setStatus(TicketStatus.SOLD);
//            ticket.setUserId(userId);
//            ticketRepository.save(ticket);
//        }
//        System.out.println(quantity + " tickets purchased.");
//    }

    public synchronized void purchaseTicket(Long eventId, Integer userId, int quantity) {
        while (systemConfigurationService.getRunningConfiguration() == null) {
            try {
                logger.info("Waiting for system configuration to be ready...");
                wait(); // Wait until the system configuration is running
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Thread interrupted while waiting for system configuration.", e);
                throw new IllegalStateException("Thread interrupted while waiting for system configuration.");
            }
        }

        boolean userExists = userRepository.existsById(userId);
        if (!userExists) {
            logger.error("User {} does not exist.", userId);
            throw new IllegalArgumentException("User does not exist.");
        }

        // Fetch available tickets for the event
        Pageable pageable = PageRequest.of(0, quantity); // Create Pageable for quantity
        List<Ticket> availableTickets = ticketRepository.findTopNAvailableTickets(eventId, pageable);

        if (availableTickets.size() < quantity) {
            logger.error("Not enough tickets available for event {}. Requested: {}, Available: {}", eventId, quantity, availableTickets.size());
            throw new IllegalStateException("Not enough tickets available.");
        }

        for (Ticket ticket : availableTickets) {
            ticket.setStatus(TicketStatus.SOLD);
            ticket.setUserId(userId);

            // Use a thread to handle each ticket's update
            new Thread(() -> {
                try {
                    Thread.sleep(1000); // Simulate processing delay
                    ticketRepository.save(ticket);
                    logger.info("Ticket {} purchased by user {} for event {}", ticket.getId(), userId, eventId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.error("Thread interrupted while purchasing ticket {} for event {}", ticket.getId(), eventId, e);
                }
            }).start();
        }

//        System.out.println(quantity + " tickets purchased by user " + userId + ".");
        logger.info("Customer {} successfully purchased {} tickets for event {}", userId, quantity, eventId);
        notifyAll(); // Notify other threads that tickets have been processed
    }

}

