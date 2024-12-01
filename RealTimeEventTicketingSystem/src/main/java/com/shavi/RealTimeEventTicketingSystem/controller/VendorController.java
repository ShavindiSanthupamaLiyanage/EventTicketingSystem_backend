package com.shavi.RealTimeEventTicketingSystem.controller;

import com.shavi.RealTimeEventTicketingSystem.entity.Event;
import com.shavi.RealTimeEventTicketingSystem.service.EventService;
//import com.shavi.RealTimeEventTicketingSystem.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vendor")
public class VendorController {

    @Autowired
    private EventService eventService;

    @PostMapping("/addEvent")
    public ResponseEntity<String> addEvent(@RequestBody Event event) {
        try {
            eventService.addEvent(event);
            return ResponseEntity.ok("Event and tickets added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }



    @GetMapping("/getEvent/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable Long eventId) {
        Event event = eventService.getEventById(eventId);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(event);
    }

    @GetMapping("/getEventByName/{eventName}")
    public ResponseEntity<Event> getEventByName(@PathVariable String eventName) {
        Event event = eventService.getEventByName(eventName);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(event);
    }

    @PutMapping("/updateEvent/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @RequestBody Event updatedEvent) {
        try {
            Event updated = eventService.updateEvent(eventId, updatedEvent);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/deleteEvent/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId) {
        try {
            eventService.deleteEvent(eventId);
            return ResponseEntity.ok("Event deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Event not found.");
        }
    }
}
