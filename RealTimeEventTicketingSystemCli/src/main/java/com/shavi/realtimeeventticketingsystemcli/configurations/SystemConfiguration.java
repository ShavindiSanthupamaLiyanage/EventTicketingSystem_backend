package com.shavi.realtimeeventticketingsystemcli.configurations;

public class SystemConfiguration extends LoggerConfiguration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    public SystemConfiguration() {
        logger.info("Creating System Configuration");
    }

    public SystemConfiguration(int totalTickets, int maxTicketCapacity,
                               int customerRetrievalRate, int ticketReleaseRate) {
        this.totalTickets = totalTickets;
        this.maxTicketCapacity = maxTicketCapacity;
        this.customerRetrievalRate = customerRetrievalRate;
        this.ticketReleaseRate = ticketReleaseRate;
        logger.info("Creating System Configuration with values: " + this.toString());
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
        logger.info("Updated total tickets to: " + totalTickets);
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
        logger.info("Updated ticket release rate to: " + ticketReleaseRate);
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
        logger.info("Updated ticket retrieval rate to: " + customerRetrievalRate);
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
        logger.info("Updated ticket capacity to: " + maxTicketCapacity);
    }

    @Override
    public String toString() {
        return "SystemConfiguration{" +
                "totalTickets=" + totalTickets +
                ", ticketReleaseRate=" + ticketReleaseRate +
                ", customerRetrievalRate=" + customerRetrievalRate +
                ", maxTicketCapacity=" + maxTicketCapacity +
                '}';
    }
}