//package com.shavi.RealTimeEventTicketingSystem.controller;
//
//import com.shavi.RealTimeEventTicketingSystem.dto.UserDto;
//import com.shavi.RealTimeEventTicketingSystem.entity.Customer;
//import com.shavi.RealTimeEventTicketingSystem.service.CustomerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/customers")
//public class CustomerController {
//
//    @Autowired
//    private CustomerService customerService;
//
//    @PostMapping("/register")
//    public ResponseEntity<String> registerCustomer(@RequestBody UserDto userDto) {
//        Customer registeredCustomer = customerService.registerCustomer(userDto);
//        return ResponseEntity.ok("Customer registered successfully with ID: " + registeredCustomer.getId());
//    }
//
//    // Get all customers
//    @GetMapping("/customers")
//    public List<Customer> getAllCustomers() {
//        return customerService.getAllCustomers();
//    }
//
//    // Get vendor by ID
//    @GetMapping("/customer/{id}")
//    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
//        Customer customer = customerService.getCustomerById(id); // This will throw ResourceNotFoundException if vendor not found
//        return ResponseEntity.ok(customer);
//    }
//
//    // Update an existing vendor
//    @PutMapping("/{id}")
//    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
//        Customer customer = customerService.updateCustomer(id, updatedCustomer); // This will throw ResourceNotFoundException if vendor not found
//        return ResponseEntity.ok(customer);
//    }
//
//    // Delete event
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
//        boolean deleted = customerService.deleteCustomer(id);
//        return deleted
//                ? ResponseEntity.ok("Customer deleted successfully.")
//                : ResponseEntity.status(404).body("Customer not found.");
//    }
//}

package com.shavi.RealTimeEventTicketingSystem.controller;

import com.shavi.RealTimeEventTicketingSystem.component.TicketPool;
import com.shavi.RealTimeEventTicketingSystem.entity.Event;
import com.shavi.RealTimeEventTicketingSystem.entity.Ticket;
//import com.shavi.RealTimeEventTicketingSystem.service.CustomerService;
import com.shavi.RealTimeEventTicketingSystem.service.EventService;
import com.shavi.RealTimeEventTicketingSystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

//    @Autowired
//    private CustomerService customerService;

//    @PostMapping("/purchase")
//    public ResponseEntity<String> purchaseTicket(@RequestBody Ticket ticket) {
//        customerService.purchaseTicket(ticket);
//        return ResponseEntity.ok("Purchase attempted.");
//    }
//}



//@PostMapping("/addEvent")
//public ResponseEntity<String> addEvent(@RequestBody Event event) {
//    try {
//        vendorService.addEvent(event);
//        return ResponseEntity.ok("Event and tickets added successfully.");
//    } catch (IllegalArgumentException e) {
//        return ResponseEntity.badRequest().body(e.getMessage());
//    } catch (IllegalStateException e) {
//        return ResponseEntity.status(500).body(e.getMessage());
//    }
//}

    @Autowired
    private EventService eventService;

    @Autowired
    private TicketService ticketService;

    // Other existing methods...

    @PostMapping("/purchaseTicket/{eventId}/{userId}")
    public ResponseEntity<String> purchaseTicket(@PathVariable Long eventId,
                                                 @PathVariable int userId,
                                                 @RequestParam int quantity) {
        try {
            // Validate ticket availability
            if (quantity <= 0) {
                return ResponseEntity.badRequest().body("Quantity must be greater than zero.");
            }

            // Call the service to purchase the ticket
            Ticket ticket = ticketService.purchaseTicket(eventId, userId, quantity);
            return ResponseEntity.ok("Ticket purchased successfully. Ticket ID: " + ticket.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while purchasing the ticket.");
        }
    }
}

