package com.shavi.RealTimeEventTicketingSystem.controller;

import com.shavi.RealTimeEventTicketingSystem.component.TicketPool;
import com.shavi.RealTimeEventTicketingSystem.component.Vendor;
import com.shavi.RealTimeEventTicketingSystem.configurations.LoggerConfiguration;
import com.shavi.RealTimeEventTicketingSystem.entity.Event;
import com.shavi.RealTimeEventTicketingSystem.repository.EventRepository;
import com.shavi.RealTimeEventTicketingSystem.service.SystemConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vendors")
public class VendorController extends LoggerConfiguration {

    @Autowired
    private TicketPool ticketPool;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @PostMapping("/addTickets")
    public ResponseEntity<String> startVendors(@RequestParam Long eventId, @RequestParam int ticketsPerVendor, @RequestParam int vendorCount) {
        logger.info("Received request to start {} vendors for event ID {} with {} tickets per vendor", vendorCount, eventId, ticketsPerVendor);

        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            logger.error("Event with ID {} not found.", eventId);
            return ResponseEntity.badRequest().body("Event not found.");
        }

        int totalTicketsNeeded = ticketsPerVendor * vendorCount;
        if (event.getNoOfTickets() < totalTicketsNeeded) {
            logger.error("Not enough tickets available for event ID {}. Requested: {}, Available: {}", eventId, totalTicketsNeeded, event.getNoOfTickets());
            return ResponseEntity.badRequest().body("Not enough tickets available for all vendors.");
        }

        if (systemConfigurationService.getRunningConfiguration() == null) {
            logger.error("System configuration is not running. Tickets cannot be added for event ID {}.", eventId);
            throw new IllegalStateException("System configuration is not running. Tickets cannot be added.");
        }

        // Start vendors
        try {
            for (int i = 0; i < vendorCount; i++) {
                Vendor vendor = new Vendor(ticketPool, eventId, ticketsPerVendor);
                Thread vendorThread = new Thread(vendor, "Vendor-" + (i + 1));
                vendorThread.start();
                logger.info("Started vendor thread for Vendor-{}", i + 1);
            }
        } catch (Exception e) {
            logger.error("Error starting vendor threads for event ID {}: {}", eventId, e.getMessage());
            return ResponseEntity.status(500).body("Error starting vendor threads.");
        }

        logger.info("Vendors started successfully for event ID {}", eventId);
        return ResponseEntity.ok("Vendors started.");
    }

}
