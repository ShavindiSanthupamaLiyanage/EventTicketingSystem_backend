package com.shavi.RealTimeEventTicketingSystem.controller;

import com.shavi.RealTimeEventTicketingSystem.entity.Event;
import com.shavi.RealTimeEventTicketingSystem.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/addEvents")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event createdEvent = eventService.addEvent(event);
        return ResponseEntity.status(201).body(createdEvent);
    }

    // Get all events
    @GetMapping("/getEvents")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }


    // Get event by ID
    @GetMapping("/getEventById/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> event = eventService.getEventById(id);
        return ResponseEntity.ok(event.get());
    }

    // Get event by name
    @GetMapping("/getEventByName/{name}")
    public ResponseEntity<List<Event>> getEventByName(@PathVariable String name) {
        List<Event> events = eventService.getEventsByName(name);
        return ResponseEntity.ok(events);
    }

    // Update event
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        Optional<Event> updatedEvent = eventService.updateEvent(id, eventDetails);
        return ResponseEntity.ok(updatedEvent.get());
    }

    // Delete event
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        boolean deleted = eventService.deleteEvent(id);
        return deleted
                ? ResponseEntity.ok("Event deleted successfully.")
                : ResponseEntity.status(404).body("Event not found.");
    }
}