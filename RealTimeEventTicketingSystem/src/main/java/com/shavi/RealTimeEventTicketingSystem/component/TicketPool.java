package com.shavi.RealTimeEventTicketingSystem.component;

import com.shavi.RealTimeEventTicketingSystem.entity.Ticket;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TicketPool {

    private final List<Ticket> tickets = Collections.synchronizedList(new ArrayList<>());

    // Add tickets to the pool
    public synchronized void addTickets(int numberOfTickets) {

//        Ticket purchasedTicket = new Ticket();
//        purchasedTicket.setEventId(eventId);
//        purchasedTicket.setUserId(userId);
//        purchasedTicket.setQuantity(quantity);
//        purchasedTicket.setStatus("PURCHASED");

        for (int i = 0; i < numberOfTickets; i++) {
            tickets.add(new Ticket());
        }
        System.out.println(numberOfTickets + " tickets added to the pool.");
        System.out.println("Current Ticket Pool Size: " + tickets.size());
    }

    public synchronized boolean checkTicketAvailability(int quantity) {
        System.out.println("Checking availability for quantity: " + quantity);
        System.out.println("Current Ticket Pool Size: " + tickets.size());
        return tickets.size() >= quantity;
    }

    public synchronized void purchaseTicket(Long eventId, Integer userId, int quantity) throws InterruptedException {
        while (tickets.isEmpty()) {
            System.out.println("No tickets available, waiting...");
            wait();
        }

        System.out.println("Attempting to purchase " + quantity + " tickets for eventId " + eventId);

        if (tickets.size() < quantity) {
            System.out.println("Not enough tickets available for purchase.");
            throw new IllegalStateException("Not enough tickets available for purchase.");
        }

//        Ticket purchasedTicket = new Ticket();
//        purchasedTicket.setEventId(eventId);
//        purchasedTicket.setUserId(userId);
//        purchasedTicket.setQuantity(quantity);
//        purchasedTicket.setStatus("PURCHASED");

        for (int i = 0; i < quantity; i++) {
            tickets.remove(0);
        }

        System.out.println(quantity + " tickets purchased. Remaining tickets: " + tickets.size());
        notifyAll();
    }

}


