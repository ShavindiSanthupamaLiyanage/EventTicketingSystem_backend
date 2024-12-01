package com.shavi.RealTimeEventTicketingSystem.service;

import com.shavi.RealTimeEventTicketingSystem.entity.Event;
import com.shavi.RealTimeEventTicketingSystem.entity.SystemConfiguration;
import com.shavi.RealTimeEventTicketingSystem.repository.EventRepository;
import com.shavi.RealTimeEventTicketingSystem.component.TicketPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @Autowired
    private TicketPool ticketPool;

    // Create an event and add the corresponding number of tickets to the pool
    public void addEvent(Event event) {
        // Fetch the running configuration to validate ticket limits
        SystemConfiguration config = systemConfigurationService.getRunningConfiguration();
        if (config == null || !config.isRunning()) {
            throw new IllegalStateException("System is not running. Cannot create events.");
        }

        // Validate ticket count
        if (event.getNoOfTickets() > config.getTotalTickets()) {
            throw new IllegalArgumentException("The number of tickets exceeds the available tickets in the system.");
        }

        // Save the event in the database
        eventRepository.save(event);

        // Add the tickets to the ticket pool
        ticketPool.addTickets(event.getNoOfTickets());
    }


    // Retrieve event details by event ID
    public Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElse(null);
    }

    // Additional methods can be added for updating or deleting events if required

    // Retrieve event by ID
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElse(null);  // Returns null if event is not found
    }

    // Retrieve event by name
    public Event getEventByName(String eventName) {
        return eventRepository.findByEventName(eventName).orElse(null);  // Returns null if event is not found
    }

    // Update event
    public Event updateEvent(Long eventId, Event updatedEvent) {
        Event existingEvent = getEventById(eventId);
        if (existingEvent == null) {
            throw new IllegalArgumentException("Event not found.");
        }

        existingEvent.setEventName(updatedEvent.getEventName());
        existingEvent.setNoOfTickets(updatedEvent.getNoOfTickets());
        existingEvent.setDate(updatedEvent.getDate());

        return eventRepository.save(existingEvent);  // Save and return the updated event
    }

    // Delete event
    public void deleteEvent(Long eventId) {
        Event event = getEventById(eventId);
        if (event == null) {
            throw new IllegalArgumentException("Event not found.");
        }

        eventRepository.delete(event);  // Delete the event from the database
    }

    // Update event when a ticket is purchased
    public void updateEventForTicketPurchase(Long eventId) {
        Event existingEvent = getEventById(eventId);
        if (existingEvent == null) {
            throw new IllegalArgumentException("Event not found.");
        }

        if (existingEvent.getNoOfTickets() <= 0) {
            throw new IllegalStateException("No tickets available for this event.");
        }

        // Decrement the ticket count
        existingEvent.setNoOfTickets(existingEvent.getNoOfTickets() - 1);

        // Save the updated event
        eventRepository.save(existingEvent);
    }

}
