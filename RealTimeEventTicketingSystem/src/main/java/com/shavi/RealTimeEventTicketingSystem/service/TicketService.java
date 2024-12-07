package com.shavi.RealTimeEventTicketingSystem.service;

import com.shavi.RealTimeEventTicketingSystem.component.TicketPool;
import com.shavi.RealTimeEventTicketingSystem.configurations.LoggerConfiguration;
import com.shavi.RealTimeEventTicketingSystem.entity.Event;
import com.shavi.RealTimeEventTicketingSystem.entity.SystemConfiguration;
import com.shavi.RealTimeEventTicketingSystem.entity.Ticket;
import com.shavi.RealTimeEventTicketingSystem.repository.EventRepository;
import com.shavi.RealTimeEventTicketingSystem.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketService extends LoggerConfiguration {

    @Autowired
    private TicketPool ticketPool;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @Autowired
    TicketRepository ticketRepository;

    public void purchaseTickets(Long eventId, Integer userId, int quantity) throws InterruptedException {
        logger.info("Received request to purchase {} tickets for event ID {} by user ID {}", quantity, eventId, userId);

        // Fetch the running configuration to validate ticket limits
        SystemConfiguration config = systemConfigurationService.getRunningConfiguration();
        if (config == null || !config.isRunning()) {
            logger.error("System is not running. Cannot process ticket purchase for event ID {}", eventId);
            throw new IllegalStateException("System is not running. Cannot create events.");
        }

        // Validate event
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found."));

        if (event.getNoOfTickets() < quantity) {
            logger.error("Not enough tickets available for event ID {}. Available: {}, Requested: {}", eventId, event.getNoOfTickets(), quantity);
            throw new IllegalStateException("Not enough tickets available.");
        }

        // Update event ticket count
        event.setNoOfTickets(event.getNoOfTickets() - quantity);
        eventRepository.save(event);
        logger.info("Event ID {} updated with new ticket count: {}", eventId, event.getNoOfTickets());

        // Purchase tickets from the ticket pool
        ticketPool.purchaseTicket(eventId, userId, quantity);
        logger.info("Purchased {} tickets for event ID {} by user ID {}", quantity, eventId, userId);
    }

    public List<Ticket> getAvailableTickets(Long eventId, int quantity) {
        logger.info("Fetching top {} available tickets for event ID {}", quantity, eventId);

        Pageable pageable = PageRequest.of(0, quantity);
        return ticketRepository.findTopNAvailableTickets(eventId, pageable);
    }

    public Map<Long, Long> getTicketCountsByUserId(Integer userId) {
        logger.info("Fetching ticket counts for user ID {}", userId);

        List<Object[]> results = ticketRepository.findTicketCountsByUserId(userId);
        Map<Long, Long> ticketCounts = new HashMap<>();
        for (Object[] result : results) {
            Long eventId = (Long) result[0];
            Long count = (Long) result[1];
            ticketCounts.put(eventId, count);
        }
        logger.info("Fetched ticket counts for user ID {}: {}", userId, ticketCounts);
        return ticketCounts;
    }

}
