package com.shavi.RealTimeEventTicketingSystem.component;

import com.shavi.RealTimeEventTicketingSystem.entity.SystemConfiguration;

public class Vendor implements Runnable {

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
            System.out.println(Thread.currentThread().getName() + " added " + ticketsToAdd + " tickets.");
        } catch (IllegalStateException e) {
            System.err.println(Thread.currentThread().getName() + ": " + e.getMessage());
        }
    }
}


