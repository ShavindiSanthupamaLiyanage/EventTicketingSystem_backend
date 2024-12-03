package com.shavi.RealTimeEventTicketingSystem.service;

import com.shavi.RealTimeEventTicketingSystem.dto.request.EventRequest;
import com.shavi.RealTimeEventTicketingSystem.entity.Event;
import com.shavi.RealTimeEventTicketingSystem.entity.SystemConfiguration;
import com.shavi.RealTimeEventTicketingSystem.repository.EventRepository;
import com.shavi.RealTimeEventTicketingSystem.component.TicketPool;
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

    // Create an event and add the corresponding number of tickets to the pool
    public void addEvent(EventRequest eventRequest) {
        // Fetch the running configuration to validate ticket limits
        SystemConfiguration config = systemConfigurationService.getRunningConfiguration();
        if (config == null || !config.isRunning()) {
            throw new IllegalStateException("System is not running. Cannot create events.");
        }

        // Validate ticket count
        if (eventRequest.getNoOfTickets() > config.getTotalTickets()) {
            throw new IllegalArgumentException("The number of tickets exceeds the available tickets in the system.");
        }

        Event event = new Event();
        event.setEventName(eventRequest.getEventName());
        event.setNoOfTickets(eventRequest.getNoOfTickets());
        event.setDate(eventRequest.getDate());
        event.setUserId(eventRequest.getUserId());


        // Save the event in the database
        Event savedEvent = eventRepository.save(event);

        // Add the tickets to the ticket pool
//        ticketPool.addTickets(event.getNoOfTickets(), savedEvent.getEventId()); // Pass event ID

        CompletableFuture.runAsync(() -> {
            try {
                ticketPool.addTickets(event.getNoOfTickets(), savedEvent.getEventId());
            } catch (Exception e) {
                // Handle or log any exceptions from the background thread
                System.err.println("Error adding tickets to the pool: " + e.getMessage());
            }
        });

    }


    // Retrieve event by ID
//    public Event getEventById(Long eventId) {
//        return eventRepository.findById(eventId).orElse(null);  // Returns null if event is not found
//    }

    public EventRequest getEventById(Long eventId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            return null;
        }
        return new EventRequest(
                event.getEventId(),
                event.getEventName(),
                event.getNoOfTickets(),
                event.getDate(),
                event.getUserId()
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
                event.getNoOfTickets(),
                event.getDate(),
                event.getUserId()
        );
    }

    // Update event
    public EventRequest updateEvent(Long eventId, EventRequest eventRequest) {
        Event existingEvent = eventRepository.findById(eventId).orElse(null);
        if (existingEvent == null) {
            throw new IllegalArgumentException("Event not found.");
        }

        existingEvent.setEventName(eventRequest.getEventName());
        existingEvent.setNoOfTickets(eventRequest.getNoOfTickets());
        existingEvent.setDate(eventRequest.getDate());

        Event updatedEvent = eventRepository.save(existingEvent);
        return new EventRequest(
                updatedEvent.getEventId(),
                updatedEvent.getEventName(),
                updatedEvent.getNoOfTickets(),
                updatedEvent.getDate(),
                updatedEvent.getUserId()
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

//    // Update event when a ticket is purchased
//    public void updateEventForTicketPurchase(Long eventId) {
//        Event existingEvent = getEventById(eventId);
//        if (existingEvent == null) {
//            throw new IllegalArgumentException("Event not found.");
//        }
//
//        if (existingEvent.getNoOfTickets() <= 0) {
//            throw new IllegalStateException("No tickets available for this event.");
//        }
//
//        // Decrement the ticket count
//        existingEvent.setNoOfTickets(existingEvent.getNoOfTickets() - 1);
//
//        // Save the updated event
//        eventRepository.save(existingEvent);
//    }

    // Retrieve all events
    public List<EventRequest> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(event -> new EventRequest(
                        event.getEventId(),
                        event.getEventName(),
                        event.getNoOfTickets(),
                        event.getDate(),
                        event.getUserId()))
                .collect(Collectors.toList());
    }

    // Retrieve events by user ID
    public List<EventRequest> getEventsByUserId(Integer userId) {
        return eventRepository.findByUserId(userId).stream()
                .map(event -> new EventRequest(
                        event.getEventId(),
                        event.getEventName(),
                        event.getNoOfTickets(),
                        event.getDate(),
                        event.getUserId()))
                .collect(Collectors.toList());
    }

}
