package com.shavi.RealTimeEventTicketingSystem.controller;

import com.shavi.RealTimeEventTicketingSystem.dto.request.EventRequest;
import com.shavi.RealTimeEventTicketingSystem.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/Event")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/addEvent")
    public ResponseEntity<Map<String, String>> addEvent(@RequestBody EventRequest eventRequest) {
        try {
            eventService.addEvent(eventRequest);

            // Prepare a success response in JSON format
            Map<String, String> response = new HashMap<>();
            response.put("message", "Event and tickets added successfully.");
            response.put("status", "success");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Prepare an error response in JSON format
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error: " + e.getMessage());
            response.put("status", "error");

            return ResponseEntity.badRequest().body(response);
        } catch (IllegalStateException e) {
            // Prepare a system error response in JSON format
            Map<String, String> response = new HashMap<>();
            response.put("message", "System Error: " + e.getMessage());
            response.put("status", "error");

            return ResponseEntity.status(500).body(response);
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
