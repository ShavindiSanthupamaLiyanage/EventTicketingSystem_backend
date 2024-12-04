package com.shavi.RealTimeEventTicketingSystem.component;

public class Customer implements Runnable {

    private final TicketPool ticketPool;
    private final Long eventId;
    private final Integer userId;
    private final int quantity;

    public Customer(TicketPool ticketPool, Long eventId, Integer userId, int quantity) {
        this.ticketPool = ticketPool;
        this.eventId = eventId;
        this.userId = userId;
        this.quantity = quantity;
    }

    @Override
    public void run() {
        try {
            ticketPool.purchaseTicket(eventId, userId, quantity);
            System.out.println("Customer " + userId + " successfully purchased " + quantity + " tickets.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.err.println("Customer " + userId + " failed to purchase tickets: " + e.getMessage());
        }
    }
}
