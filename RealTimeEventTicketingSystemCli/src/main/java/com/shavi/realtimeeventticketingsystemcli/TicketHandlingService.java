//package com.shavi.realtimeeventticketingsystemcli;
//
//import com.shavi.realtimeeventticketingsystemcli.TicketPool;
//import com.shavi.realtimeeventticketingsystemcli.configurations.LoggerConfiguration;
//import com.shavi.realtimeeventticketingsystemcli.Vendor;
//import com.shavi.realtimeeventticketingsystemcli.Customer;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//public class TicketHandlingService extends LoggerConfiguration {
//
//    private final ExecutorService vendorService;
//    private final ExecutorService customerService;
//    private final AtomicBoolean isRunning;
//    private final TicketPool ticketPool;
//    private final int ticketReleaseRate;
//
//    public TicketHandlingService(TicketPool ticketPool, int ticketReleaseRate) {
//        this.vendorService = Executors.newFixedThreadPool(3);
//        this.customerService = Executors.newFixedThreadPool(5);
//        this.isRunning = new AtomicBoolean(false);
//        this.ticketPool = ticketPool;
//        this.ticketReleaseRate = ticketReleaseRate;
//    }
//
//    public void startTicketingOperations() {
//        if (isRunning.compareAndSet(false, true)) {
//            logger.info("Starting ticket handling operations...");
//            for (int i = 1; i <= 3; i++) {
//                Vendor vendor = new Vendor(i, ticketReleaseRate, 1000, ticketPool);
//                vendorService.execute(vendor);
//            }
//            for (int i = 1; i <= 5; i++) {
//                Customer customer = new Customer(i, ticketPool, 2000, 2);
//                customerService.execute(customer);
//            }
//        } else {
//            logger.warning("Ticket handling is already running.");
//        }
//    }
//
//    public void stopTicketingOperations() {
//        if (isRunning.compareAndSet(true, false)) {
//            logger.info("Stopping ticket handling operations...");
//            vendorService.shutdownNow();
//            customerService.shutdownNow();
//        } else {
//            logger.warning("Ticket handling is not currently running.");
//        }
//    }
//}
