package com.shavi.RealTimeEventTicketingSystem.component;

import com.shavi.RealTimeEventTicketingSystem.configurations.LoggerConfiguration;

public class Customer extends LoggerConfiguration implements Runnable {

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
            logger.info("Customer {} successfully purchased {} tickets for event {}.", userId, quantity, eventId);
        } catch (IllegalStateException | IllegalArgumentException e) {
            logger.error("Customer {} failed to purchase tickets for event {}: {}", userId, eventId, e.getMessage());
        }
    }
}
