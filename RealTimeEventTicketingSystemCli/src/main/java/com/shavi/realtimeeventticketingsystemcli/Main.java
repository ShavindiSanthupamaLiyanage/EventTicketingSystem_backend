//package com.shavi.realtimeeventticketingsystemcli;
//
//import com.shavi.realtimeeventticketingsystemcli.configuration.LoggerConfiguration;
//import com.shavi.realtimeeventticketingsystemcli.configuration.SystemConfiguration;
//
//import java.util.Scanner;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.io.OutputStream;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class Main extends LoggerConfiguration {
//
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Welcome to the Ticketing System CLI");
//        logger.info("Starting the Ticketing System CLI");
//
//        int totalTickets = getValidInput(scanner, "Enter total tickets (0 or positive):", 1, Integer.MAX_VALUE);
//        int ticketReleaseRate = getValidInput(scanner, "Enter ticket release rate (0 to 100):", 1, 100);
//        int customerRetrievalRate = getValidInput(scanner, "Enter customer retrieval rate (1 to 10):", 1, 10);
//        int maxTicketCapacity = getValidInput(scanner, "Enter maximum ticket capacity (0 or positive):", 1, Integer.MAX_VALUE);
//
//        // Create a SystemConfiguration object
//        SystemConfiguration config = new SystemConfiguration(totalTickets, maxTicketCapacity, customerRetrievalRate, ticketReleaseRate);
//
//        // Send data to backend
//        sendConfigToBackend(config);
//
//
//
//
//        // Set up TicketPool with maxTicketCapacity
//        TicketPool ticketPool = new TicketPool(maxTicketCapacity);
//
//        // Start vendor threads for concurrent ticket releasing
//        ExecutorService vendorService = Executors.newFixedThreadPool(3); // Adjust the number of vendors as needed
//        for (int i = 1; i <= 3 ; i++) {  // Create 3 vendors as an example
//            Vendor vendor = new Vendor(i, ticketReleaseRate, 2000, ticketPool);  // Release tickets every 2 seconds
//            vendorService.execute(vendor);
//        }
//
//        // Start customer threads for purchasing tickets
//        ExecutorService customerService = Executors.newFixedThreadPool(5); // Adjust the number of customers as needed
//        for (int i = 1; i <= 5; i++) {  // Create 5 customers as an example
//            //int retrievalInterval = 1000; // 1 second interval for each customer
//            int ticketsPerRetrieval = 2; // Each customer attempts to retrieve 2 tickets at a time
//            Customer customer = new Customer(i, ticketPool, 1000, ticketsPerRetrieval);
//            customerService.execute(customer);
//        }
//
//        // Optionally shutdown services after some time to stop all vendors and customers (e.g., after 30 seconds)
//        vendorService.shutdown();
//        customerService.shutdown();
//        // Shutdown executor after some time to stop all vendors (e.g., after 10 seconds)
//        //executorService.shutdown();
//
//        // Await termination if desired
//        try {
//            if (!vendorService.awaitTermination(30, java.util.concurrent.TimeUnit.SECONDS)) {
//                vendorService.shutdownNow(); // Force shutdown if not terminated
//            }
//            if (!customerService.awaitTermination(30, java.util.concurrent.TimeUnit.SECONDS)) {
//                customerService.shutdownNow(); // Force shutdown if not terminated
//            }
//        } catch (InterruptedException e) {
//            vendorService.shutdownNow();
//            customerService.shutdownNow();
//            logger.severe("Error during termination of service: " + e.getMessage());
//        }
//    }
//
//    private static int getValidInput(Scanner scanner, String prompt, int min, int max) {
//        int input;
//        while (true) {
//            System.out.print(prompt);
//            try {
//                input = Integer.parseInt(scanner.nextLine().trim());
//                if (input < min || input > max) {
//                    System.out.printf("Invalid input! Please enter a number between " + min + " and " + max);
//                    logger.warning("Invalid input! Number should be in between " + min + " and " + max);
//                } else {
//                    break;
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("Invalid input! Please enter a valid integer.");
//                logger.warning("Invalid input! Please enter a valid integer.");
//            }
//        }
//        return input;
//    }
//
//    public static void sendConfigToBackend(SystemConfiguration config) {
//        // Create configuration data as JSON
//        String configData = String.format(
//                "{\"totalTickets\": %d, \"ticketReleaseRate\": %d, \"customerRetrievalRate\": %d, \"maxTicketCapacity\": %d}",
//                config.getTotalTickets(), config.getTicketReleaseRate(),
//                config.getCustomerRetrievalRate(), config.getMaxTicketCapacity());
//        try {
//            URL url = new URL("http://localhost:8080/api/cliConfiguration/post/configure");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setDoOutput(true);
//
//            // Write config data to request body
//            OutputStream os = connection.getOutputStream();
//            os.write(configData.getBytes());
//            os.flush();
//            os.close();
//
//            // Check the response
//            int responseCode = connection.getResponseCode();
//            logger.info("Response Code: " + responseCode);
//
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                logger.info("Configuration saved successfully.");
//            } else {
//                logger.warning("Failed to save configuration. Response Code: " + responseCode);
//                System.out.println("There was an issue connecting to the server. Please try again later.");
//            }
//        } catch (java.net.ConnectException e) {
//            // This handles when the server is not reachable (connection refused)
//            logger.severe("Connection refused. Could not connect to the server at localhost:8080.");
//            System.out.println("Unable to connect to the server. Please check if the backend is running and try again.");
//        } catch (java.io.IOException e) {
//            // Handles IOExceptions, such as issues opening the connection
//            logger.severe("Error occurred while sending configuration to backend: " + e.getMessage());
//            System.out.println("There was an error sending data to the server. Please check your network connection and try again.");
//        } catch (Exception e) {
//            // General exception handling
//            logger.severe("Unexpected error occurred: " + e.getMessage());
//            System.out.println("An unexpected error occurred. Please try again later.");
//        }
//    }
//
//}




package com.shavi.realtimeeventticketingsystemcli;

import com.shavi.realtimeeventticketingsystemcli.configurations.LoggerConfiguration;
import com.shavi.realtimeeventticketingsystemcli.configurations.SystemConfiguration;

import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main extends LoggerConfiguration {

    // Atomic flag to control ticketing operations
    private static final AtomicBoolean isRunning = new AtomicBoolean(false);
    private static ExecutorService vendorService;
    private static ExecutorService customerService;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Ticketing System CLI");
        logger.info("Starting the Ticketing System CLI");

        int totalTickets = getValidInput(scanner, "Enter total tickets (0 or positive):", 1, Integer.MAX_VALUE);
        int ticketReleaseRate = getValidInput(scanner, "Enter ticket release rate (0 to 100):", 1, 100);
        int customerRetrievalRate = getValidInput(scanner, "Enter customer retrieval rate (1 to 10):", 1, 10);
        int maxTicketCapacity = getValidInput(scanner, "Enter maximum ticket capacity (0 or positive):", 1, Integer.MAX_VALUE);

        // Create a SystemConfiguration object
        SystemConfiguration config = new SystemConfiguration(totalTickets, maxTicketCapacity, customerRetrievalRate, ticketReleaseRate);

        // Send data to backend
        sendConfigToBackend(config);

        TicketPool ticketPool = new TicketPool(maxTicketCapacity);

        while (true) {
            if (!isRunning.get()) { // Only prompt for input if ticket handling is not running
                System.out.println("Enter '1' to START ticket handling, '2' to PAUSE, or '0' to EXIT:");
            }

            String command = scanner.nextLine().trim().toLowerCase();

            if ("1".equals(command)) {
                if (isRunning.compareAndSet(false, true)) {
                    System.out.println("Starting ticket handling operations...");
                    vendorService = Executors.newFixedThreadPool(3); // Reinitialize vendorService
                    customerService = Executors.newFixedThreadPool(5); // Reinitialize customerService
                    startTicketingOperations(vendorService, customerService, ticketPool, ticketReleaseRate);
                } else {
                    System.out.println("Ticket handling is already running.");
                }
            } else if ("2".equals(command)) {
                if (isRunning.compareAndSet(true, false)) {
                    System.out.println("Stopping ticket handling operations...");
                    stopTicketingOperations(vendorService, customerService);
                } else {
                    System.out.println("Ticket handling is not currently running.");
                }
            } else if ("0".equals(command)) {
                System.out.println("Exiting the system. Goodbye!");
                logger.info("Exiting the Ticketing System CLI");
                stopTicketingOperations(vendorService, customerService);
                break;
            } else {
                System.out.println("Invalid command! Please enter '1', '2', or '0'.");
            }
        }
    }

    private static void startTicketingOperations(ExecutorService vendorService, ExecutorService customerService, TicketPool ticketPool, int ticketReleaseRate) {
        // Start vendor threads for concurrent ticket releasing
        for (int i = 1; i <= 3; i++) {  // Create 3 vendors as an example
            Vendor vendor = new Vendor(i, ticketReleaseRate, 2000, ticketPool);  // Release tickets every 2 seconds
            vendorService.execute(vendor);
        }

        // Start customer threads for purchasing tickets
        for (int i = 1; i <= 5; i++) {  // Create 5 customers as an example
            int ticketsPerRetrieval = 2; // Each customer attempts to retrieve 2 tickets at a time
            Customer customer = new Customer(i, ticketPool, 1000, ticketsPerRetrieval);
            customerService.execute(customer);
        }
    }

    private static void stopTicketingOperations(ExecutorService vendorService, ExecutorService customerService) {
        // Attempt to stop the services gracefully
        if (vendorService != null && !vendorService.isShutdown()) {
            vendorService.shutdownNow();
        }
        if (customerService != null && !customerService.isShutdown()) {
            customerService.shutdownNow();
        }

        try {
            if (vendorService != null && !vendorService.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                vendorService.shutdownNow();
            }
            if (customerService != null && !customerService.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                customerService.shutdownNow();
            }
        } catch (InterruptedException e) {
            if (vendorService != null) vendorService.shutdownNow();
            if (customerService != null) customerService.shutdownNow();
            logger.severe("Error during shutdown of services: " + e.getMessage());
        }
    }

    private static int getValidInput(Scanner scanner, String prompt, int min, int max) {
        int input;
        while (true) {
            System.out.print(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine().trim());
                if (input < min || input > max) {
                    System.out.printf("Invalid input! Please enter a number between " + min + " and " + max);
                    logger.warning("Invalid input! Number should be in between " + min + " and " + max);
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid integer.");
                logger.warning("Invalid input! Please enter a valid integer.");
            }
        }
        return input;
    }

    public static void sendConfigToBackend(SystemConfiguration config) {
        // Create configuration data as JSON
        String configData = String.format(
                "{\"totalTickets\": %d, \"ticketReleaseRate\": %d, \"customerRetrievalRate\": %d, \"maxTicketCapacity\": %d}",
                config.getTotalTickets(), config.getTicketReleaseRate(),
                config.getCustomerRetrievalRate(), config.getMaxTicketCapacity());
        try {
            URL url = new URL("http://localhost:8080/api/cliConfiguration/post/configure");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Write config data to request body
            OutputStream os = connection.getOutputStream();
            os.write(configData.getBytes());
            os.flush();
            os.close();

            // Check the response
            int responseCode = connection.getResponseCode();
            logger.info("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                logger.info("Configuration saved successfully.");
            } else {
                logger.warning("Failed to save configuration. Response Code: " + responseCode);
                System.out.println("There was an issue connecting to the server. Please try again later.");
            }
        } catch (java.net.ConnectException e) {
            logger.severe("Connection refused. Could not connect to the server at localhost:8080.");
            System.out.println("Unable to connect to the server. Please check if the backend is running and try again.");
        } catch (java.io.IOException e) {
            logger.severe("Error occurred while sending configuration to backend: " + e.getMessage());
            System.out.println("There was an error sending data to the server. Please check your network connection and try again.");
        } catch (Exception e) {
            logger.severe("Unexpected error occurred: " + e.getMessage());
            System.out.println("An unexpected error occurred. Please try again later.");
        }
    }
}



