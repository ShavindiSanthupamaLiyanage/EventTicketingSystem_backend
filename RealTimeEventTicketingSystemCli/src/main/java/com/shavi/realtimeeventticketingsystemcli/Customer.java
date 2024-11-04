package com.shavi.realtimeeventticketingsystemcli;

public class Customer extends LoggerConfiguration implements Runnable {
    private final int customerId; // Unique identifier for the customer
    private final TicketPool ticketPool; // Reference to the shared TicketPool
    private final int retrievalInterval; // Interval for ticket purchase attempts
    private final int ticketsPerRetrieval; // Number of tickets to retrieve per attempt

    public Customer(int customerId, TicketPool ticketPool, int retrievalInterval, int ticketsPerRetrieval) {
        this.customerId = customerId;
        this.ticketPool = ticketPool;
        this.retrievalInterval = retrievalInterval;
        this.ticketsPerRetrieval = ticketsPerRetrieval; // Set tickets per retrieval
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Attempt to retrieve the specified number of tickets from the pool
                for (int i = 0; i < ticketsPerRetrieval; i++) {
                    Ticket ticket = ticketPool.retrieveTicket();
                    if (ticket != null) {
                        System.out.printf("Customer %d successfully purchased ticket %d%n", customerId, ticket.getId());
                        logger.info("Customer " + customerId + " successfully purchased ticket " + ticket.getId()); // Log successful purchase
                    } else {
                        logger.info("Customer " + customerId + " found no tickets available."); // Log when no tickets are available
                        break; // If no ticket is returned, break the loop
                    }
                }
                // Wait for the specified interval before the next purchase attempt
                Thread.sleep(retrievalInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
                logger.warning("Customer " + customerId + " could not purchase tickets at this time. Tickets may become available shortly."); // Log warning
                System.out.printf("Customer " + customerId + " could not purchase tickets at this time. Tickets may become available shortly. Please hold on for the next release %n");
                break;
            }
        }
    }
}