//package com.shavi.RealTimeEventTicketingSystem.component;
//
//import com.shavi.RealTimeEventTicketingSystem.entity.Ticket;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//@Component
//public class TicketPool {
//
//    private final List<Ticket> tickets = Collections.synchronizedList(new ArrayList<>());
//
//    // Add tickets to the pool (no changes needed here)
//    public synchronized void addTickets(int numberOfTickets) {
//        for (int i = 0; i < numberOfTickets; i++) {
//            tickets.add(new Ticket());
//        }
//        System.out.println(numberOfTickets + " tickets added to the pool.");
//    }
//
//    // Modify the purchaseTicket method to accept the eventId, userId, and quantity
//    public synchronized void purchaseTicket(Long eventId, Integer userId, int quantity) throws InterruptedException {
//        while (tickets.isEmpty()) {
//            wait();  // Wait until a ticket is available
//        }
//
//        // Ensure the ticket pool has enough tickets
//        if (tickets.size() < quantity) {
//            throw new IllegalStateException("Not enough tickets available for purchase.");
//        }
//
//        // Create tickets based on quantity and assign eventId, userId, and quantity
//        Ticket purchasedTicket = new Ticket();
//        purchasedTicket.setEventId(eventId);
//        purchasedTicket.setUserId(userId);
//        purchasedTicket.setQuantity(quantity);
//        purchasedTicket.setStatus("PURCHASED");
//
//        // Deduct the required number of tickets from the pool
//        for (int i = 0; i < quantity; i++) {
//            tickets.remove(0);  // Remove tickets from the pool
//        }
//
//        // Notify other threads that tickets have been purchased
//        notifyAll();
//
//    }
//}
//
//
//


package com.shavi.RealTimeEventTicketingSystem.component;

import com.shavi.RealTimeEventTicketingSystem.entity.Ticket;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TicketPool {

    private final List<Ticket> tickets = Collections.synchronizedList(new ArrayList<>());

    // Add tickets to the pool (no changes needed here)
    public synchronized void addTickets(int numberOfTickets) {
        for (int i = 0; i < numberOfTickets; i++) {
            tickets.add(new Ticket());
        }
        System.out.println(numberOfTickets + " tickets added to the pool.");
    }

    // Method to check if enough tickets are available
    public synchronized boolean checkTicketAvailability(int quantity) {
        // If the number of available tickets is greater than or equal to the quantity requested, return true
        return tickets.size() >= quantity;
    }

    // Modify the purchaseTicket method to accept the eventId, userId, and quantity
    public synchronized void purchaseTicket(Long eventId, Integer userId, int quantity) throws InterruptedException {
        while (tickets.isEmpty()) {
            wait();  // Wait until a ticket is available
        }

        // Ensure the ticket pool has enough tickets
        if (tickets.size() < quantity) {
            throw new IllegalStateException("Not enough tickets available for purchase.");
        }

        // Create tickets based on quantity and assign eventId, userId, and quantity
        Ticket purchasedTicket = new Ticket();
        purchasedTicket.setEventId(eventId);
        purchasedTicket.setUserId(userId);
        purchasedTicket.setQuantity(quantity);
        purchasedTicket.setStatus("PURCHASED");

        // Deduct the required number of tickets from the pool
        for (int i = 0; i < quantity; i++) {
            tickets.remove(0);  // Remove tickets from the pool
        }

        // Notify other threads that tickets have been purchased
        notifyAll();
    }
}
