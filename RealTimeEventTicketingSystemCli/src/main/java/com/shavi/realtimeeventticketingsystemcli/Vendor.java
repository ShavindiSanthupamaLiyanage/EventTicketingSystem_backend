package com.shavi.realtimeeventticketingsystemcli;

import com.shavi.realtimeeventticketingsystemcli.configurations.LoggerConfiguration;

public class Vendor extends LoggerConfiguration implements Runnable {
    private final int vendorId;
    private final int ticketsPerRelease;
    private final int releaseInterval;
    private final TicketPool ticketPool;

    public Vendor(int vendorId, int ticketsPerRelease, int releaseInterval, TicketPool ticketPool) {
        this.vendorId = vendorId;
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseInterval = releaseInterval;
        this.ticketPool = ticketPool;
    }

    @Override
    public void run() {
        try {
            while (true) {
                for (int i = 0; i < ticketsPerRelease; i++) {
                    Ticket ticket = new Ticket(vendorId);
                    ticketPool.addTicket(ticket, vendorId); // Add ticket to the pool
                    //System.out.println("Vendor " + vendorId + " added ticket " + ticket);
                    logger.info("Vendor " + vendorId + " added ticket: " + ticket.getId()); // Log ticket addition
                }
                Thread.sleep(releaseInterval); // Wait for the specified release interval
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warning("Vendor " + vendorId + " was unable to release tickets due to high demand. Please wait for the next ticket release cycle."); // Log warning
            System.out.println("Vendor " + vendorId + " was unable to release tickets due to high demand. Please wait for the next ticket release cycle.");
        }
    }
}


