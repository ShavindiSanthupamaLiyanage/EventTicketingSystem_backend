package com.shavi.RealTimeEventTicketingSystem.controller;

import com.shavi.RealTimeEventTicketingSystem.configurations.LoggerConfiguration;
import com.shavi.RealTimeEventTicketingSystem.dto.request.EventRequest;
import com.shavi.RealTimeEventTicketingSystem.service.EventService;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/Event")
public class EventController extends LoggerConfiguration {

    @Autowired
    private EventService eventService;

    @PostMapping("/addEvent")
    public ResponseEntity<Map<String, String>> addEvent(@RequestBody EventRequest eventRequest) {
        logger.info("Received request to add event: {}", eventRequest);
        try {
            eventService.addEvent(eventRequest);
            logger.info("Event added successfully: {}", eventRequest);

            // Prepare a success response in JSON format
            Map<String, String> response = new HashMap<>();
            response.put("message", "Event and tickets added successfully.");
            response.put("status", "success");

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Error adding event: {}", e.getMessage());
            // Prepare an error response in JSON format
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error: " + e.getMessage());
            response.put("status", "error");

            return ResponseEntity.badRequest().body(response);
        } catch (IllegalStateException e) {
            logger.error("System error while adding event: {}", e.getMessage());
            // Prepare a system error response in JSON format
            Map<String, String> response = new HashMap<>();
            response.put("message", "System Error: " + e.getMessage());
            response.put("status", "error");

            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/getEvent/{eventId}")
    public ResponseEntity<Object> getEvent(@PathVariable Long eventId) {
        logger.info("Fetching event with ID: {}", eventId);

        EventRequest eventRequest = eventService.getEventById(eventId);
        if (eventRequest == null) {
            return ResponseEntity.status(404).body("Event with ID " + eventId + " not found.");
        }
        logger.info("Event fetched successfully with ID: {}", eventId);
        return ResponseEntity.ok(eventRequest);
    }

    @GetMapping("/getEventByName/{eventName}")
    public ResponseEntity<Object> getEventByName(@PathVariable String eventName) {
        logger.info("Fetching event with name: {}", eventName);

        EventRequest eventResponse = eventService.getEventByName(eventName);
        if (eventResponse == null) {
            logger.warn("Event with name '{}' not found.", eventName);
            return ResponseEntity.status(404).body("Event with name \"" + eventName + "\" not found.");
        }
        logger.info("Event fetched successfully with name: {}", eventName);
        return ResponseEntity.ok(eventResponse);
    }

    @PutMapping("/updateEvent/{eventId}")
    public ResponseEntity<Object> updateEvent(
            @PathVariable Long eventId,
            @RequestBody EventRequest eventRequest) {
        logger.info("Received request to update event with ID: {}", eventId);

        try {
            EventRequest updatedEvent = eventService.updateEvent(eventId, eventRequest);
            logger.info("Event updated successfully with ID: {}", eventId);
            return ResponseEntity.ok(updatedEvent);
        } catch (IllegalArgumentException e) {
            logger.error("Error updating event with ID {}: {}", eventId, e.getMessage());
            return ResponseEntity.badRequest().body("Unable to update event: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteEvent/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
        logger.info("Received request to delete event with ID: {}", eventId);
        try {
            eventService.deleteEvent(eventId);
            logger.info("Event deleted successfully with ID: {}", eventId);
            return ResponseEntity.ok("Event deleted successfully.");
        } catch (IllegalArgumentException e) {
            logger.error("Error deleting event with ID {}: {}", eventId, e.getMessage());
            return ResponseEntity.badRequest().body("Error: Event not found.");
        }
    }

    // Get all events
    @GetMapping("/getAllEvents")
    public ResponseEntity<Object> getAllEvents() {
        logger.info("Fetching all events.");

        List<EventRequest> events = eventService.getAllEvents();
        if (events.isEmpty()) {
            logger.warn("No events found.");
            return ResponseEntity.status(204).body("No events found.");
        }
        logger.info("Fetched {} events.", events.size());
        return ResponseEntity.ok(events);
    }

    // Get events by user ID
    @GetMapping("/getEventsByUserId/{userId}")
    public ResponseEntity<Object> getEventsByUserId(@PathVariable Integer userId) {
        logger.info("Fetching events for user with ID: {}", userId);

        List<EventRequest> events = eventService.getEventsByUserId(userId);
        if (events.isEmpty()) {
            logger.warn("No events found for user with ID {}", userId);
            return ResponseEntity.status(204).body("No events found for user with ID " + userId + ".");
        }
        logger.info("Fetched {} events for user with ID {}", events.size(), userId);
        return ResponseEntity.ok(events);
    }
}
