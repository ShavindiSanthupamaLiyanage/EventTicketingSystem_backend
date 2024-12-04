package com.shavi.RealTimeEventTicketingSystem.controller;

import com.shavi.RealTimeEventTicketingSystem.component.TicketPool;
import com.shavi.RealTimeEventTicketingSystem.component.Vendor;
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
public class VendorController {

    @Autowired
    private TicketPool ticketPool;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @PostMapping("/addTickets")
    public ResponseEntity<String> startVendors(@RequestParam Long eventId, @RequestParam int ticketsPerVendor, @RequestParam int vendorCount) {
        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            return ResponseEntity.badRequest().body("Event not found.");
        }

        int totalTicketsNeeded = ticketsPerVendor * vendorCount;
        if (event.getNoOfTickets() < totalTicketsNeeded) {
            return ResponseEntity.badRequest().body("Not enough tickets available for all vendors.");
        }

        if (systemConfigurationService.getRunningConfiguration() == null) {
            throw new IllegalStateException("System configuration is not running. Tickets cannot be added.");
        }

        for (int i = 0; i < vendorCount; i++) {
            Vendor vendor = new Vendor(ticketPool, eventId, ticketsPerVendor);
            Thread vendorThread = new Thread(vendor, "Vendor-" + (i + 1));
            vendorThread.start();
        }
        return ResponseEntity.ok("Vendors started.");
    }

}
