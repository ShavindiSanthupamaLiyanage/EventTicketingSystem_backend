package com.shavi.RealTimeEventTicketingSystem.controller;

import com.shavi.RealTimeEventTicketingSystem.dto.request.EventRequest;
import com.shavi.RealTimeEventTicketingSystem.entity.Event;
import com.shavi.RealTimeEventTicketingSystem.repository.EventRepository;
import com.shavi.RealTimeEventTicketingSystem.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Event")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/addEvent")
    public ResponseEntity<String> addEvent(@RequestBody EventRequest eventRequest) {
        try {
            eventService.addEvent(eventRequest);
            return ResponseEntity.ok("Event and tickets added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(("Error: " + e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(500).body("System Error: " + e.getMessage());
        }
    }

    @GetMapping("/getEvent/{eventId}")
    public ResponseEntity<Object> getEvent(@PathVariable Long eventId) {
        EventRequest eventRequest = eventService.getEventById(eventId);
        if (eventRequest == null) {
            return ResponseEntity.status(404).body("Event with ID " + eventId + " not found.");
        }
        return ResponseEntity.ok(eventRequest);
    }


    @GetMapping("/getEventByName/{eventName}")
    public ResponseEntity<Object> getEventByName(@PathVariable String eventName) {
        EventRequest eventResponse = eventService.getEventByName(eventName);
        if (eventResponse == null) {
            return ResponseEntity.status(404).body("Event with name \"" + eventName + "\" not found.");
        }
        return ResponseEntity.ok(eventResponse);
    }

    @PutMapping("/updateEvent/{eventId}")
    public ResponseEntity<Object> updateEvent(
            @PathVariable Long eventId,
            @RequestBody EventRequest eventRequest) {
        try {
            EventRequest updatedEvent = eventService.updateEvent(eventId, eventRequest);
            return ResponseEntity.ok(updatedEvent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Unable to update event: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteEvent/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
        try {
            eventService.deleteEvent(eventId);
            return ResponseEntity.ok("Event deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: Event not found.");
        }
    }

    // Get all events
    @GetMapping("/getAllEvents")
    public ResponseEntity<Object> getAllEvents() {
        List<EventRequest> events = eventService.getAllEvents();
        if (events.isEmpty()) {
            return ResponseEntity.status(204).body("No events found.");
        }
        return ResponseEntity.ok(events);
    }

    // Get events by user ID
    @GetMapping("/getEventsByUserId/{userId}")
    public ResponseEntity<Object> getEventsByUserId(@PathVariable Integer userId) {
        List<EventRequest> events = eventService.getEventsByUserId(userId);
        if (events.isEmpty()) {
            return ResponseEntity.status(204).body("No events found for user with ID " + userId + ".");
        }
        return ResponseEntity.ok(events);
    }

}
