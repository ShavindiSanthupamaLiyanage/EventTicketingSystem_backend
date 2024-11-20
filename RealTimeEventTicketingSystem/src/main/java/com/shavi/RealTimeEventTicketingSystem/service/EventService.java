package com.shavi.RealTimeEventTicketingSystem.service;

import com.shavi.RealTimeEventTicketingSystem.entity.Event;
import com.shavi.RealTimeEventTicketingSystem.exception.ValidationException;
import com.shavi.RealTimeEventTicketingSystem.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event addEvent(Event event) {
        //****
        event.setAvailableTickets(event.getTotalTickets());
        validateEvent(event);
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public List<Event> getEventsByName(String name) {
        return eventRepository.findByNameContainingIgnoreCase(name);
    }

    public Optional<Event> updateEvent(Long id, Event eventDetails) {
        return eventRepository.findById(id).map(event -> {
            event.setName(eventDetails.getName());
            event.setDate(eventDetails.getDate());
            event.setLocation(eventDetails.getLocation());
            event.setVendor(eventDetails.getVendor());
            event.setAvailableTickets(eventDetails.getTotalTickets());
            event.setTotalTickets(eventDetails.getTotalTickets());
            event.setAvailableTickets(eventDetails.getAvailableTickets());
            event.setSoldTickets(eventDetails.getSoldTickets());
            event.setTicketPrice(eventDetails.getTicketPrice());

            // Ensure the status from the request is set
            event.setStatus(eventDetails.getStatus());

            validateEvent(event);
            return eventRepository.save(event);
        });
    }

    public boolean deleteEvent(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private void validateEvent(Event event) {
        List<String> errors = new ArrayList<>();
        if (event.getName() == null || event.getName().isEmpty()) {
            errors.add("Event name must be provided.");
        }
        if (event.getDate() == null) {
            errors.add("Event date must be provided.");
        }
        if (event.getLocation() == null || event.getLocation().isEmpty()) {
            errors.add("Event location must be provided.");
        }
        if (event.getVendor() == null || event.getVendor().getId() == null) {
            errors.add("Vendor ID must be provided.");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
