package com.shavi.realtimeeventticketingsystemcli;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TicketPool extends LoggerConfiguration {
    private final List<Ticket> tickets;
    private final int maxCapacity;

    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.tickets = Collections.synchronizedList(new LinkedList<>()); // Thread-safe list
        logger.info("Ticket pool initialized with maxCapacity: " + maxCapacity);
    }

    // Adds tickets to the pool, ensuring thread safety and waiting if pool is at capacity
    public synchronized void addTicket(Ticket ticket, int vendorId) {
        while (tickets.size() >= maxCapacity) {
            System.out.println("Ticket pool is at max capacity. Vendor " + vendorId + " is waiting to add more tickets...");
            logger.info("Ticket pool is at max capacity: " + maxCapacity +". Vendor " + vendorId + " is waiting to add more tickets...");
            try {
                wait();  // Wait for space in the pool
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                //System.out.println("Vendor was interrupted while waiting to add tickets.");
                logger.severe("Vendor was interrupted while waiting to add tickets.");
                return;
            }
        }
        tickets.add(ticket);
        System.out.println("Vendor " + vendorId + " successfully added ticket ID " + ticket.getId());
        logger.info("Vendor " + vendorId + " successfully added ticket ID " + ticket.getId());
        notifyAll();  // Notify any waiting consumers
    }

    // Removes a ticket from the pool for a customer, waiting if no tickets are available
    public synchronized Ticket retrieveTicket() {
        while (tickets.isEmpty()) {
            System.out.println("No tickets available. Customer is waiting for tickets to be added...");
            logger.info("Customer is waiting for tickets to be added...");
            try {
                wait();  // Wait for a ticket to be available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                //System.out.println("Customer was interrupted while waiting for tickets.");
                logger.severe("Customer was interrupted while waiting for tickets.");
                return null;
            }
        }
        Ticket ticket = tickets.remove(0); // Remove ticket from pool
        //System.out.println("Customer retrieved ticket ID " + ticket.getId());
        logger.info("Customer retrieved ticket ID " + ticket.getId());
        notifyAll();  // Notify any waiting vendors
        return ticket;
    }

    // Check current pool size (for monitoring purposes)
    public synchronized int getCurrentSize() {
        int size = tickets.size();
        logger.info("Current ticket pool size: " + size);
        return size;
    }
}