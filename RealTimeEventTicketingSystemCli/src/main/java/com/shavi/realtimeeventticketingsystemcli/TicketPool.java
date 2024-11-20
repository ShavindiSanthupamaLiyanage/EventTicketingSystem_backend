//package com.shavi.realtimeeventticketingsystemcli;
//
//import com.shavi.realtimeeventticketingsystemcli.configurations.LoggerConfiguration;
//
//import java.util.Collections;
//import java.util.LinkedList;
//import java.util.List;
//
//public class TicketPool extends LoggerConfiguration {
//    private final List<Ticket> tickets;
//    private final int maxCapacity;
//
//    public TicketPool(int maxCapacity) {
//        this.maxCapacity = maxCapacity;
//        this.tickets = Collections.synchronizedList(new LinkedList<>()); // Thread-safe list
//        logger.info("Ticket pool initialized with maxCapacity: " + maxCapacity);
//    }
//
//    // Adds tickets to the pool, ensuring thread safety and waiting if pool is at capacity
//    public synchronized void addTicket(Ticket ticket, int vendorId) {
//        while (tickets.size() >= maxCapacity) {
//            System.out.println("Ticket pool is at max capacity. Vendor " + vendorId + " is waiting to add more tickets...");
//            logger.info("Ticket pool is at max capacity: " + maxCapacity +". Vendor " + vendorId + " is waiting to add more tickets...");
//            try {
//                wait();  // Wait for space in the pool
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                //System.out.println("Vendor was interrupted while waiting to add tickets.");
//                logger.severe("Vendor was interrupted while waiting to add tickets.");
//                return;
//            }
//        }
//        tickets.add(ticket);
//        System.out.println("Vendor " + vendorId + " successfully added ticket ID " + ticket.getId());
//        logger.info("Vendor " + vendorId + " successfully added ticket ID " + ticket.getId());
//        notifyAll();  // Notify any waiting consumers
//    }
//
//    // Removes a ticket from the pool for a customer, waiting if no tickets are available
//    public synchronized Ticket retrieveTicket() {
//        while (tickets.isEmpty()) {
//            System.out.println("No tickets available. Customer is waiting for tickets to be added...");
//            logger.info("Customer is waiting for tickets to be added...");
//            try {
//                wait();  // Wait for a ticket to be available
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                //System.out.println("Customer was interrupted while waiting for tickets.");
//                logger.severe("Customer was interrupted while waiting for tickets.");
//                return null;
//            }
//        }
//        Ticket ticket = tickets.remove(0); // Remove ticket from pool
//        //System.out.println("Customer retrieved ticket ID " + ticket.getId());
//        logger.info("Customer retrieved ticket ID " + ticket.getId());
//        notifyAll();  // Notify any waiting vendors
//        return ticket;
//    }
//
//    // Check current pool size
//    public synchronized int getCurrentSize() {
//        int size = tickets.size();
//        logger.info("Current ticket pool size: " + size);
//        return size;
//    }
//}



//package com.shavi.realtimeeventticketingsystemcli;
//
//import com.shavi.realtimeeventticketingsystemcli.configurations.LoggerConfiguration;
//
//import java.util.Collections;
//import java.util.LinkedList;
//import java.util.List;
//
//public class TicketPool extends LoggerConfiguration {
//    private final List<Ticket> tickets;
//    private final int maxCapacity; // Maximum number of tickets in the pool at one time
//    private final int totalTickets; // Total tickets that can be produced
//    private int totalTicketsProduced = 0; // Count of total tickets produced
//
//    public TicketPool(int maxCapacity, int totalTickets) {
//        this.maxCapacity = maxCapacity;
//        this.totalTickets = totalTickets;
//        this.tickets = Collections.synchronizedList(new LinkedList<>()); // Thread-safe list
//        logger.info("Ticket pool initialized with maxCapacity: " + maxCapacity + " and totalTickets: " + totalTickets);
//    }
//
//    // Adds a ticket to the pool, respecting capacity and totalTickets constraints
//    public synchronized void addTicket(Ticket ticket, int vendorId) {
//        // Wait if the pool is at capacity or all tickets have been produced
//        while (tickets.size() >= maxCapacity || totalTicketsProduced >= totalTickets) {
//            if (totalTicketsProduced >= totalTickets) {
//                logger.info("All tickets have been produced. Vendor " + vendorId + " cannot add more tickets.");
//                return; // Stop adding tickets if totalTickets limit is reached
//            }
//            System.out.println("Ticket pool is at max capacity. Vendor " + vendorId + " is waiting to add tickets...");
//            logger.info("Vendor " + vendorId + " is waiting to add tickets...");
//            try {
//                wait(); // Wait until space is available
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                logger.severe("Vendor was interrupted while waiting to add tickets.");
//                return;
//            }
//        }
//
//        // Add the ticket to the pool
//        tickets.add(ticket);
//        totalTicketsProduced++;
//        System.out.println("Vendor " + vendorId + " added ticket ID " + ticket.getId());
//        logger.info("Vendor " + vendorId + " added ticket ID " + ticket.getId());
//        notifyAll(); // Notify any waiting consumers
//    }
//
//    // Retrieves a ticket from the pool for a customer
//    public synchronized Ticket retrieveTicket() {
//        // Wait if the pool is empty
//        while (tickets.isEmpty()) {
//            if (totalTicketsProduced >= totalTickets) {
//                logger.info("All tickets have been sold. No more tickets available.");
//                return null; // No tickets available, and all have been produced
//            }
//            System.out.println("No tickets available. Customer is waiting...");
//            logger.info("Customer is waiting for tickets...");
//            try {
//                wait(); // Wait until a ticket is available
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                logger.severe("Customer was interrupted while waiting for tickets.");
//                return null;
//            }
//        }
//
//        // Remove and return a ticket from the pool
//        Ticket ticket = tickets.remove(0);
//        System.out.println("Customer retrieved ticket ID " + ticket.getId());
//        logger.info("Customer retrieved ticket ID " + ticket.getId());
//        notifyAll(); // Notify any waiting vendors
//        return ticket;
//    }
//
//    // Gets the current size of the ticket pool
//    public synchronized int getCurrentSize() {
//        int size = tickets.size();
//        logger.info("Current ticket pool size: " + size);
//        return size;
//    }
//
//    // Gets the total number of tickets produced
//    public synchronized int getTotalTicketsProduced() {
//        return totalTicketsProduced;
//    }
//
//    // Gets the total number of tickets allowed
//    public int getTotalTickets() {
//        return totalTickets;
//    }
//}



package com.shavi.realtimeeventticketingsystemcli;

import com.shavi.realtimeeventticketingsystemcli.configurations.LoggerConfiguration;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TicketPool extends LoggerConfiguration {
    private final List<Ticket> tickets;
    private final int maxCapacity; // Maximum number of tickets in the pool at one time
    private int totalTicketsProduced = 0; // Count of total tickets produced

    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.tickets = Collections.synchronizedList(new LinkedList<>()); // Thread-safe list
        logger.info("Ticket pool initialized with maxCapacity: " + maxCapacity);
    }

    // Adds a ticket to the pool, respecting capacity constraints
    public synchronized void addTicket(Ticket ticket, int vendorId) {
        // Wait if the pool is at capacity
        while (tickets.size() >= maxCapacity) {
            System.out.println("Ticket pool is at max capacity. Vendor " + vendorId + " is waiting to add tickets...");
            logger.info("Vendor " + vendorId + " is waiting to add tickets...");
            try {
                wait(); // Wait until space is available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.severe("Vendor was interrupted while waiting to add tickets.");
                return;
            }
        }

        // Add the ticket to the pool
        tickets.add(ticket);
        totalTicketsProduced++;
        System.out.println("Vendor " + vendorId + " added ticket ID " + ticket.getId());
        logger.info("Vendor " + vendorId + " added ticket ID " + ticket.getId());
        notifyAll(); // Notify any waiting consumers
    }

    // Retrieves a ticket from the pool for a customer
    public synchronized Ticket retrieveTicket() {
        // Wait if the pool is empty
        while (tickets.isEmpty()) {
            System.out.println("No tickets available. Customer is waiting...");
            logger.info("Customer is waiting for tickets...");
            try {
                wait(); // Wait until a ticket is available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.severe("Customer was interrupted while waiting for tickets.");
                return null;
            }
        }

        // Remove and return a ticket from the pool
        Ticket ticket = tickets.remove(0);
        System.out.println("Customer retrieved ticket ID " + ticket.getId());
        logger.info("Customer retrieved ticket ID " + ticket.getId());
        notifyAll(); // Notify any waiting vendors
        return ticket;
    }

    // Gets the current size of the ticket pool
    public synchronized int getCurrentSize() {
        int size = tickets.size();
        logger.info("Current ticket pool size: " + size);
        return size;
    }
}
