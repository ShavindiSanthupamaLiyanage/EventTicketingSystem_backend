package com.shavi.RealTimeEventTicketingSystem.service;

import com.shavi.RealTimeEventTicketingSystem.configurations.LoggerConfiguration;
import com.shavi.RealTimeEventTicketingSystem.dto.request.EventRequest;
import com.shavi.RealTimeEventTicketingSystem.entity.Event;
import com.shavi.RealTimeEventTicketingSystem.entity.SystemConfiguration;
import com.shavi.RealTimeEventTicketingSystem.enums.TicketStatus;
import com.shavi.RealTimeEventTicketingSystem.repository.EventRepository;
import com.shavi.RealTimeEventTicketingSystem.component.TicketPool;
import com.shavi.RealTimeEventTicketingSystem.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class EventService extends LoggerConfiguration {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @Autowired
    private TicketPool ticketPool;

    @Autowired
    private TicketRepository ticketRepository;

    public void addEvent(EventRequest eventRequest) {
        logger.info("Received request to add event: {}", eventRequest);

        // Fetch the running configuration to validate ticket limits
        SystemConfiguration config = systemConfigurationService.getRunningConfiguration();
        if (config == null || !config.isRunning()) {
            logger.error("System is not running. Cannot create events.");
            throw new IllegalStateException("System is not running. Cannot create events.");
        }

        // Create a new event
        Event event = new Event();
        event.setEventName(eventRequest.getEventName());
        event.setNoOfTickets(eventRequest.getNoOfTickets());
        event.setTotalTickets(eventRequest.getNoOfTickets());
        event.setDate(eventRequest.getDate());
        event.setVenue(eventRequest.getVenue());
        event.setDescription(eventRequest.getDescription());
        event.setStatus(eventRequest.getStatus());
        event.setUserId(eventRequest.getUserId());
        event.setTicketPrice(eventRequest.getTicketPrice());
        event.setCategory(eventRequest.getCategory());


        // Save the event in the database
        Event savedEvent = eventRepository.save(event);
        logger.info("Event saved successfully with ID: {}", savedEvent.getEventId());


        // Now that the event is saved, you can safely access its eventId
        long currentTickets = ticketRepository.countByEventIdAndStatus(savedEvent.getEventId(), TicketStatus.AVAILABLE);

        // Validate if the added tickets exceed the total number of tickets for the event
        if (currentTickets + eventRequest.getNoOfTickets() > savedEvent.getNoOfTickets()) {
            logger.error("Adding tickets exceeds the event's total ticket limit.");
            throw new IllegalStateException("Adding these tickets exceeds the event's total ticket limit.");
        }

        // Add the tickets to the ticket pool asynchronously
        Event finalSavedEvent = savedEvent;
        CompletableFuture.runAsync(() -> {
            try {
                ticketPool.addTickets(event.getNoOfTickets(), finalSavedEvent.getEventId());
                logger.info("Tickets added to the pool for event ID: {}", finalSavedEvent.getEventId());

            } catch (Exception e) {
                logger.error("Error adding tickets to the pool: {}", e.getMessage());
            }
        });
    }

    public EventRequest getEventById(Long eventId) {
        logger.info("Fetching event with ID: {}", eventId);

        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            logger.warn("Event with ID {} not found.", eventId);
            return null;
        }
        logger.info("Event fetched successfully with ID: {}", eventId);
        return new EventRequest(
                event.getEventId(),
                event.getEventName(),
                event.getTicketPrice(),
                event.getTotalTickets(),
                event.getNoOfTickets(),
                event.getDate(),
                event.getUserId(),
                event.getVenue(),
                event.getDescription(),
                event.getCategory(),
                event.getStatus()
        );
    }

    // Retrieve event by name
    public EventRequest getEventByName(String eventName) {
        logger.info("Fetching event with name: {}", eventName);

        Event event = eventRepository.findByEventName(eventName).orElse(null);
        if (event == null) {
            logger.warn("Event with name '{}' not found.", eventName);
            return null;
        }
        logger.info("Event fetched successfully with name: {}", eventName);
        return new EventRequest(
                event.getEventId(),
                event.getEventName(),
                event.getTicketPrice(),
                event.getNoOfTickets(),
                event.getTotalTickets(),
                event.getDate(),
                event.getUserId(),
                event.getVenue(),
                event.getDescription(),
                event.getCategory(),
                event.getStatus()
        );
    }

    // Update event
    public EventRequest updateEvent(Long eventId, EventRequest eventRequest) {
        logger.info("Received request to update event with ID: {}", eventId);

        Event existingEvent = eventRepository.findById(eventId).orElse(null);
        if (existingEvent == null) {
            logger.error("Event with ID {} not found for update.", eventId);
            throw new IllegalArgumentException("Event not found.");
        }

        existingEvent.setEventName(eventRequest.getEventName());
        existingEvent.setTicketPrice(eventRequest.getTicketPrice());
        existingEvent.setNoOfTickets(eventRequest.getNoOfTickets());
        existingEvent.setTotalTickets(eventRequest.getNoOfTickets());
        existingEvent.setDate(eventRequest.getDate());
        existingEvent.setVenue(eventRequest.getVenue());
        existingEvent.setDescription(eventRequest.getDescription());
        existingEvent.setCategory(eventRequest.getCategory());
        existingEvent.setStatus(eventRequest.getStatus());

        Event updatedEvent = eventRepository.save(existingEvent);
        logger.info("Event updated successfully with ID: {}", updatedEvent.getEventId());
        return new EventRequest(
                updatedEvent.getEventId(),
                updatedEvent.getEventName(),
                updatedEvent.getTicketPrice(),
                updatedEvent.getNoOfTickets(),
                updatedEvent.getTotalTickets(),
                updatedEvent.getDate(),
                updatedEvent.getUserId(),
                updatedEvent.getVenue(),
                updatedEvent.getDescription(),
                updatedEvent.getCategory(),
                updatedEvent.getStatus()
        );
    }

    // Delete event
    public void deleteEvent(Long eventId) {
        logger.info("Received request to delete event with ID: {}", eventId);

        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            logger.error("Event with ID {} not found for deletion.", eventId);
            throw new IllegalArgumentException("Event not found.");
        }
        eventRepository.delete(event);
        logger.info("Event deleted successfully with ID: {}", eventId);
    }

    // Retrieve all events
    public List<EventRequest> getAllEvents() {
        logger.info("Fetching all events.");
        return eventRepository.findAll().stream()
                .map(event -> new EventRequest(
                        event.getEventId(),
                        event.getEventName(),
                        event.getTicketPrice(),
                        event.getNoOfTickets(),
                        event.getTotalTickets(),
                        event.getDate(),
                        event.getUserId(),
                        event.getVenue(), // Return venue
                        event.getDescription(), // Return description
                        event.getCategory(), // Return category
                        event.getStatus() // Return eventCategory
                ))
                .collect(Collectors.toList());
    }

    // Retrieve events by user ID
    public List<EventRequest> getEventsByUserId(Integer userId) {
        logger.info("Fetching events for user with ID: {}", userId);

        List<EventRequest> events = eventRepository.findByUserId(userId).stream()
                .map(event -> new EventRequest(
                        event.getEventId(),
                        event.getEventName(),
                        event.getTicketPrice(),
                        event.getNoOfTickets(),
                        event.getTotalTickets(),
                        event.getDate(),
                        event.getUserId(),
                        event.getVenue(),
                        event.getDescription(),
                        event.getCategory(),
                        event.getStatus()
                ))
                .collect(Collectors.toList());

        logger.info("Fetched {} events for user with ID: {}", events.size(), userId);
        return events;
    }
}
