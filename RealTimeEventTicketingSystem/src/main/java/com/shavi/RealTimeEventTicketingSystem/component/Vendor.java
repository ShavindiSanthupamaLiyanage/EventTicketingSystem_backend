package com.shavi.RealTimeEventTicketingSystem.component;

import com.shavi.RealTimeEventTicketingSystem.configurations.LoggerConfiguration;
import com.shavi.RealTimeEventTicketingSystem.entity.SystemConfiguration;

public class Vendor extends LoggerConfiguration implements Runnable {

    private final TicketPool ticketPool;
    private final Long eventId;
    private final int ticketsToAdd;

    public Vendor(TicketPool ticketPool, Long eventId, int ticketsToAdd) {
        this.ticketPool = ticketPool;
        this.eventId = eventId;
        this.ticketsToAdd = ticketsToAdd;
    }

    @Override
    public void run() {
        try {
            ticketPool.addTickets(ticketsToAdd, eventId);
            logger.info(Thread.currentThread().getName() + " added " + ticketsToAdd + " tickets.");
        } catch (IllegalStateException e) {
            logger.error(Thread.currentThread().getName() + ": " + e.getMessage(), e);
        }
    }
}


