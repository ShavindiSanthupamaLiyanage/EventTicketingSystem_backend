package com.shavi.RealTimeEventTicketingSystem.service;

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
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @Autowired
    private TicketPool ticketPool;

    @Autowired
    private TicketRepository ticketRepository;

    public void addEvent(EventRequest eventRequest) {
        // Fetch the running configuration to validate ticket limits
        SystemConfiguration config = systemConfigurationService.getRunningConfiguration();
        if (config == null || !config.isRunning()) {
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

        // Now that the event is saved, you can safely access its eventId
        long currentTickets = ticketRepository.countByEventIdAndStatus(savedEvent.getEventId(), TicketStatus.AVAILABLE);

        // Validate if the added tickets exceed the total number of tickets for the event
        if (currentTickets + eventRequest.getNoOfTickets() > savedEvent.getNoOfTickets()) {
            throw new IllegalStateException("Adding these tickets exceeds the event's total ticket limit.");
        }

        // Add the tickets to the ticket pool asynchronously
        Event finalSavedEvent = savedEvent;
        CompletableFuture.runAsync(() -> {
            try {
                ticketPool.addTickets(event.getNoOfTickets(), finalSavedEvent.getEventId());
            } catch (Exception e) {
                // Handle or log any exceptions from the background thread
                System.err.println("Error adding tickets to the pool: " + e.getMessage());
            }
        });
    }

    public EventRequest getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            return null;
        }
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
        Event event = eventRepository.findByEventName(eventName).orElse(null);
        if (event == null) {
            return null;
        }
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
        Event existingEvent = eventRepository.findById(eventId).orElse(null);
        if (existingEvent == null) {
            throw new IllegalArgumentException("Event not found.");
        }

        existingEvent.setEventName(eventRequest.getEventName());
        existingEvent.setTicketPrice(eventRequest.getTicketPrice());
        existingEvent.setNoOfTickets(eventRequest.getNoOfTickets());
        existingEvent.setTotalTickets(eventRequest.getNoOfTickets());
        existingEvent.setDate(eventRequest.getDate());
        existingEvent.setVenue(eventRequest.getVenue()); // Update venue
        existingEvent.setDescription(eventRequest.getDescription()); // Update description
        existingEvent.setCategory(eventRequest.getCategory()); // Update category
        existingEvent.setStatus(eventRequest.getStatus()); // Update event category

        Event updatedEvent = eventRepository.save(existingEvent);
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
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            throw new IllegalArgumentException("Event not found.");
        }
        eventRepository.delete(event);
    }

    // Retrieve all events
    public List<EventRequest> getAllEvents() {
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
        return eventRepository.findByUserId(userId).stream()
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
    }
}
