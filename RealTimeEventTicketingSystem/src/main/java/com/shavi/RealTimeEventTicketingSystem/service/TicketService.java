package com.shavi.RealTimeEventTicketingSystem.service;

import com.shavi.RealTimeEventTicketingSystem.entity.Event;
import com.shavi.RealTimeEventTicketingSystem.entity.Ticket;
import com.shavi.RealTimeEventTicketingSystem.entity.Customer;
import com.shavi.RealTimeEventTicketingSystem.enums.EventStatus;
import com.shavi.RealTimeEventTicketingSystem.repository.EventRepository;
import com.shavi.RealTimeEventTicketingSystem.repository.TicketRepository;
import com.shavi.RealTimeEventTicketingSystem.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class TicketService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public String buyTicket(Long eventId, Long customerId, int quantity) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        if (event.getStatus() != EventStatus.UPCOMING) {
            return "Tickets cannot be purchased for a non-upcoming event.";
        }

        int ticketsSold = ticketRepository.sumTicketsByEvent(eventId);
        int availableTickets = event.getTotalTickets() - ticketsSold;

        if (quantity > availableTickets) {
            return "Not enough tickets available.";
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setCustomer(customer);
        ticket.setQuantity(quantity);
        ticket.setPurchaseDate(LocalDate.now());
        //ticket.getPrice();
        ticket.setPrice(event.getTicketPrice());

        ticketRepository.save(ticket);

        // Update available and sold tickets
        event.setAvailableTickets(event.getAvailableTickets() - quantity);
        event.setSoldTickets(event.getSoldTickets() + quantity);

        eventRepository.save(event);

        return "Ticket purchase successful!";
    }

    // New method: Get available tickets for an event
    public int getAvailableTickets(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));
        int ticketsSold = ticketRepository.sumTicketsByEvent(eventId);
        return event.getTotalTickets() - ticketsSold;
    }

    // New method: Add tickets to an event
    @Transactional
    public String addTickets(Long eventId, int quantity) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        if (quantity <= 0) {
            return "Quantity must be greater than zero.";
        }

        event.setAvailableTickets(event.getAvailableTickets() + quantity);
        event.setTotalTickets(event.getTotalTickets() + quantity);

        eventRepository.save(event);
        return "Tickets added successfully!";
    }
}
