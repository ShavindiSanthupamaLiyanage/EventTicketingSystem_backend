////package com.shavi.realtimeeventticketingsystemcli;
////
////import com.shavi.realtimeeventticketingsystemcli.configurations.LoggerConfiguration;
////import com.shavi.realtimeeventticketingsystemcli.configurations.SystemConfiguration;
////
////import java.util.Scanner;
////import java.net.HttpURLConnection;
////import java.net.URL;
////import java.io.OutputStream;
////import java.util.concurrent.ExecutorService;
////import java.util.concurrent.Executors;
////
////public class Main extends LoggerConfiguration {
////
////    public static void main(String[] args) {
////        Scanner scanner = new Scanner(System.in);
////        System.out.println("Welcome to the Ticketing System CLI");
////        logger.info("Starting the Ticketing System CLI");
////
////        int totalTickets = getValidInput(scanner, "Enter total tickets (0 or positive):", 1, Integer.MAX_VALUE);
////        int ticketReleaseRate = getValidInput(scanner, "Enter ticket release rate (0 to 100):", 1, 100);
////        int customerRetrievalRate = getValidInput(scanner, "Enter customer retrieval rate (1 to 10):", 1, 10);
////        int maxTicketCapacity = getValidInput(scanner, "Enter maximum ticket capacity (0 or positive):", 1, Integer.MAX_VALUE);
////
////        // Create a SystemConfiguration object
////        SystemConfiguration config = new SystemConfiguration(totalTickets, maxTicketCapacity, customerRetrievalRate, ticketReleaseRate);
////
////        // Send data to backend
////        sendConfigToBackend(config);
////
////
////
////
////        // Set up TicketPool with maxTicketCapacity
////        TicketPool ticketPool = new TicketPool(maxTicketCapacity);
////
////        // Start vendor threads for concurrent ticket releasing
////        ExecutorService vendorService = Executors.newFixedThreadPool(3); // Adjust the number of vendors as needed
////        for (int i = 1; i <= 3 ; i++) {  // Create 3 vendors as an example
////            Vendor vendor = new Vendor(i, ticketReleaseRate, 2000, ticketPool);  // Release tickets every 2 seconds
////            vendorService.execute(vendor);
////        }
////
////        // Start customer threads for purchasing tickets
////        ExecutorService customerService = Executors.newFixedThreadPool(5); // Adjust the number of customers as needed
////        for (int i = 1; i <= 5; i++) {  // Create 5 customers as an example
////            //int retrievalInterval = 1000; // 1 second interval for each customer
////            int ticketsPerRetrieval = 2; // Each customer attempts to retrieve 2 tickets at a time
////            Customer customer = new Customer(i, ticketPool, 1000, ticketsPerRetrieval);
////            customerService.execute(customer);
////        }
////
////        // Optionally shutdown services after some time to stop all vendors and customers (e.g., after 30 seconds)
////        vendorService.shutdown();
////        customerService.shutdown();
////        // Shutdown executor after some time to stop all vendors (e.g., after 10 seconds)
////        //executorService.shutdown();
////
////        // Await termination if desired
////        try {
////            if (!vendorService.awaitTermination(30, java.util.concurrent.TimeUnit.SECONDS)) {
////                vendorService.shutdownNow(); // Force shutdown if not terminated
////            }
////            if (!customerService.awaitTermination(30, java.util.concurrent.TimeUnit.SECONDS)) {
////                customerService.shutdownNow(); // Force shutdown if not terminated
////            }
////        } catch (InterruptedException e) {
////            vendorService.shutdownNow();
////            customerService.shutdownNow();
////            logger.severe("Error during termination of service: " + e.getMessage());
////        }
////    }
////
////    private static int getValidInput(Scanner scanner, String prompt, int min, int max) {
////        int input;
////        while (true) {
////            System.out.print(prompt);
////            try {
////                input = Integer.parseInt(scanner.nextLine().trim());
////                if (input < min || input > max) {
////                    System.out.printf("Invalid input! Please enter a number between " + min + " and " + max);
////                    logger.warning("Invalid input! Number should be in between " + min + " and " + max);
////                } else {
////                    break;
////                }
////            } catch (NumberFormatException e) {
////                System.out.println("Invalid input! Please enter a valid integer.");
////                logger.warning("Invalid input! Please enter a valid integer.");
////            }
////        }
////        return input;
////    }
////
////    public static void sendConfigToBackend(SystemConfiguration config) {
////        // Create configuration data as JSON
////        String configData = String.format(
////                "{\"totalTickets\": %d, \"ticketReleaseRate\": %d, \"customerRetrievalRate\": %d, \"maxTicketCapacity\": %d}",
////                config.getTotalTickets(), config.getTicketReleaseRate(),
////                config.getCustomerRetrievalRate(), config.getMaxTicketCapacity());
////        try {
////            URL url = new URL("http://localhost:8080/api/cliConfiguration/post/configure");
////            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
////            connection.setRequestMethod("POST");
////            connection.setRequestProperty("Content-Type", "application/json");
////            connection.setDoOutput(true);
////
////            // Write config data to request body
////            OutputStream os = connection.getOutputStream();
////            os.write(configData.getBytes());
////            os.flush();
////            os.close();
////
////            // Check the response
////            int responseCode = connection.getResponseCode();
////            logger.info("Response Code: " + responseCode);
////
////            if (responseCode == HttpURLConnection.HTTP_OK) {
////                logger.info("Configuration saved successfully.");
////            } else {
////                logger.warning("Failed to save configuration. Response Code: " + responseCode);
////                System.out.println("There was an issue connecting to the server. Please try again later.");
////            }
////        } catch (java.net.ConnectException e) {
////            // This handles when the server is not reachable (connection refused)
////            logger.severe("Connection refused. Could not connect to the server at localhost:8080.");
////            System.out.println("Unable to connect to the server. Please check if the backend is running and try again.");
////        } catch (java.io.IOException e) {
////            // Handles IOExceptions, such as issues opening the connection
////            logger.severe("Error occurred while sending configuration to backend: " + e.getMessage());
////            System.out.println("There was an error sending data to the server. Please check your network connection and try again.");
////        } catch (Exception e) {
////            // General exception handling
////            logger.severe("Unexpected error occurred: " + e.getMessage());
////            System.out.println("An unexpected error occurred. Please try again later.");
////        }
////    }
////
////}
//
//
//
//
package com.shavi.realtimeeventticketingsystemcli;

import com.shavi.realtimeeventticketingsystemcli.configurations.LoggerConfiguration;
import com.shavi.realtimeeventticketingsystemcli.configurations.SystemConfiguration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

////public class Main extends LoggerConfiguration {
////
////    private static final AtomicBoolean isRunning = new AtomicBoolean(false);
////    private static ExecutorService vendorService;
////    private static ExecutorService customerService;
////
////    public static void main(String[] args) {
////        Scanner scanner = new Scanner(System.in);
////        System.out.println("Welcome to the Ticketing System CLI");
////        logger.info("Starting the Ticketing System CLI");
////
////        int totalTickets = getValidInput(scanner, "Enter total tickets (0 or positive):", 1, Integer.MAX_VALUE);
////        int ticketReleaseRate = getValidInput(scanner, "Enter ticket release rate (0 to 100):", 1, 100);
////        int customerRetrievalRate = getValidInput(scanner, "Enter customer retrieval rate (1 to 10):", 1, 10);
////        int maxTicketCapacity = getValidInput(scanner, "Enter maximum ticket capacity (0 or positive):", 1, Integer.MAX_VALUE);
////
////        // Create a SystemConfiguration object
////        SystemConfiguration config = new SystemConfiguration(totalTickets, maxTicketCapacity, customerRetrievalRate, ticketReleaseRate);
////
////        // Send data to backend
////        sendConfigToBackend(config);
////
////        TicketPool ticketPool = new TicketPool(maxTicketCapacity);
////
////        while (true) {
////            if (!isRunning.get()) { // Only prompt for input if ticket handling is not running
////                System.out.println("Enter '1' to START ticket handling, '2' to PAUSE, or '0' to EXIT:");
////            }
////
////            String command = scanner.nextLine().trim().toLowerCase();
////
////            if ("1".equals(command)) {
////                if (isRunning.compareAndSet(false, true)) {
////                    System.out.println("Starting ticket handling operations...");
////                    vendorService = Executors.newFixedThreadPool(3); // Reinitialize vendorService
////                    customerService = Executors.newFixedThreadPool(5); // Reinitialize customerService
////                    startTicketingOperations(vendorService, customerService, ticketPool, ticketReleaseRate, totalTickets);
////                } else {
////                    System.out.println("Ticket handling is already running.");
////                }
////            } else if ("2".equals(command)) {
////                if (isRunning.compareAndSet(true, false)) {
////                    System.out.println("Stopping ticket handling operations...");
////                    stopTicketingOperations(vendorService, customerService);
////                } else {
////                    System.out.println("Ticket handling is not currently running.");
////                }
////            } else if ("0".equals(command)) {
////                System.out.println("Exiting the system. Goodbye!");
////                logger.info("Exiting the Ticketing System CLI");
////                stopTicketingOperations(vendorService, customerService);
////                break;
////            } else {
////                System.out.println("Invalid command! Please enter '1', '2', or '0'.");
////            }
////        }
////    }
////
////    private static void startTicketingOperations(ExecutorService vendorService, ExecutorService customerService, TicketPool ticketPool, int ticketReleaseRate,int totalTickets) {
////        // Start vendor threads for concurrent ticket releasing
////        for (int i = 1; i <= 3; i++) {  // Create 3 vendors as an example
////            Vendor vendor = new Vendor(i, ticketReleaseRate, 1000, ticketPool, totalTickets);  // Release tickets every 2 seconds
////            vendorService.execute(vendor);
////        }
////
////        // Start customer threads for purchasing tickets
////        for (int i = 1; i <= 5; i++) {  // Create 5 customers as an example
////            int ticketsPerRetrieval = 2; // Each customer attempts to retrieve 2 tickets at a time
////            Customer customer = new Customer(i, ticketPool, 2000, ticketsPerRetrieval);
////            customerService.execute(customer);
////        }
////
////        // Start monitoring the ticketing system
////        new Thread(() -> monitorTicketingOperations(ticketPool)).start();
////    }
////
////    private static void stopTicketingOperations(ExecutorService vendorService, ExecutorService customerService) {
////        // Attempt to stop the services gracefully
////        if (vendorService != null && !vendorService.isShutdown()) {
////            vendorService.shutdownNow();
////        }
////        if (customerService != null && !customerService.isShutdown()) {
////            customerService.shutdownNow();
////        }
////
////        try {
////            if (vendorService != null && !vendorService.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
////                vendorService.shutdownNow();
////            }
////            if (customerService != null && !customerService.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
////                customerService.shutdownNow();
////            }
////        } catch (InterruptedException e) {
////            if (vendorService != null) vendorService.shutdownNow();
////            if (customerService != null) customerService.shutdownNow();
////            logger.severe("Error during shutdown of services: " + e.getMessage());
////        }
////    }
////
////    private static void monitorTicketingOperations(TicketPool ticketPool) {
////        while (isRunning.get()) {
////            try {
////                int currentSize = ticketPool.getCurrentSize();
////                logger.info("Monitoring Ticket Pool: Current Size = " + currentSize);
////                System.out.println("Monitoring Ticket Pool: Current Size = " + currentSize);
////
////                // Check for specific conditions, e.g., low tickets
////                if (currentSize == 0) {
////                    logger.warning("Warning: Ticket pool is empty!");
////                    System.out.println("Warning: Ticket pool is empty!");
////                }
////
////                Thread.sleep(5000); // Monitor every 5 seconds
////            } catch (InterruptedException e) {
////                Thread.currentThread().interrupt();
////                logger.warning("Monitoring thread interrupted.");
////                break;
////            }
////        }
////    }
////
////    private static int getValidInput(Scanner scanner, String prompt, int min, int max) {
////        int input;
////        while (true) {
////            System.out.print(prompt);
////            try {
////                input = Integer.parseInt(scanner.nextLine().trim());
////                if (input < min || input > max) {
////                    System.out.printf("Invalid input! Please enter a number between " + min + " and " + max);
////                    logger.warning("Invalid input! Number should be in between " + min + " and " + max);
////                } else {
////                    break;
////                }
////            } catch (NumberFormatException e) {
////                System.out.println("Invalid input! Please enter a valid integer.");
////                logger.warning("Invalid input! Please enter a valid integer.");
////            }
////        }
////        return input;
////    }
////
////    public static void sendConfigToBackend(SystemConfiguration config) {
////        // Create configuration data as JSON
////        String configData = String.format(
////                "{\"totalTickets\": %d, \"ticketReleaseRate\": %d, \"customerRetrievalRate\": %d, \"maxTicketCapacity\": %d}",
////                config.getTotalTickets(), config.getTicketReleaseRate(),
////                config.getCustomerRetrievalRate(), config.getMaxTicketCapacity());
////        try {
////            URL url = new URL("http://localhost:8080/api/cliConfiguration/post/configure");
////            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
////            connection.setRequestMethod("POST");
////            connection.setRequestProperty("Content-Type", "application/json");
////            connection.setDoOutput(true);
////
////            // Write config data to request body
////            OutputStream os = connection.getOutputStream();
////            os.write(configData.getBytes());
////            os.flush();
////            os.close();
////
////            // Check the response
////            int responseCode = connection.getResponseCode();
////            logger.info("Response Code: " + responseCode);
////
////            if (responseCode == HttpURLConnection.HTTP_OK) {
////                logger.info("Configuration saved successfully.");
////            } else {
////                logger.warning("Failed to save configuration. Response Code: " + responseCode);
////                System.out.println("There was an issue connecting to the server. Please try again later.");
////            }
////        } catch (java.net.ConnectException e) {
////            logger.severe("Connection refused. Could not connect to the server at localhost:8080.");
////            System.out.println("Unable to connect to the server. Please check if the backend is running and try again.");
////        } catch (java.io.IOException e) {
////            logger.severe("Error occurred while sending configuration to backend: " + e.getMessage());
////            System.out.println("There was an error sending data to the server. Please check your network connection and try again.");
////        } catch (Exception e) {
////            logger.severe("Unexpected error occurred: " + e.getMessage());
////            System.out.println("An unexpected error occurred. Please try again later.");
////        }
////    }
////}
//
public class Main extends LoggerConfiguration {

    private static final AtomicBoolean isRunning = new AtomicBoolean(false);
    private static ExecutorService vendorService;
    private static ExecutorService customerService;
    private static volatile boolean isMonitoring = true;  // Flag to control monitoring thread

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
                    startTicketingOperations(vendorService, customerService, ticketPool, ticketReleaseRate, totalTickets);
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
                isMonitoring = false;  // Stop the monitoring thread
                break;
            } else {
                System.out.println("Invalid command! Please enter '1', '2', or '0'.");
            }
        }
        scanner.close();
    }

    private static void startTicketingOperations(ExecutorService vendorService, ExecutorService customerService, TicketPool ticketPool, int ticketReleaseRate, int totalTickets) {
        // Start vendor threads for concurrent ticket releasing
        for (int i = 1; i <= 3; i++) {  // Create 3 vendors as an example
            Vendor vendor = new Vendor(i, ticketReleaseRate, 1000, ticketPool, totalTickets);  // Release tickets every 2 seconds
            vendorService.execute(vendor);
        }

        // Start customer threads for purchasing tickets
        for (int i = 1; i <= 5; i++) {  // Create 5 customers as an example
            int ticketsPerRetrieval = 2; // Each customer attempts to retrieve 2 tickets at a time
            Customer customer = new Customer(i, ticketPool, 2000, ticketsPerRetrieval);
            customerService.execute(customer);
        }

        // Start monitoring the ticketing system
        new Thread(() -> monitorTicketingOperations(ticketPool)).start();
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

    private static void monitorTicketingOperations(TicketPool ticketPool) {
        while (isMonitoring) {  // Check if monitoring should continue
            try {
                int currentSize = ticketPool.getCurrentSize();
                logger.info("Monitoring Ticket Pool: Current Size = " + currentSize);
                System.out.println("Monitoring Ticket Pool: Current Size = " + currentSize);

                // Check for specific conditions, e.g., low tickets
                if (currentSize == 0) {
                    logger.warning("Warning: Ticket pool is empty!");
                    System.out.println("Warning: Ticket pool is empty!");
                    // Optionally, pause the ticket handling process if the pool is empty
                }

                Thread.sleep(5000); // Monitor every 5 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warning("Monitoring thread interrupted.");
                break;
            }
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

    private static void monitorTicketPool(Long eventId) {
        while (isMonitoring) {
            try {
                URL url = new URL("http://localhost:8080/tickets/availability?eventId=" + eventId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int availableTickets;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    availableTickets = Integer.parseInt(reader.readLine());
                }

                System.out.println("Monitoring Ticket Pool: Available Tickets = " + availableTickets);
                if (availableTickets == 0) {
                    System.out.println("Warning: No tickets available for this event!");
                }

                Thread.sleep(5000); // Check every 5 seconds
            } catch (Exception e) {
                System.out.println("Error monitoring ticket pool: " + e.getMessage());
            }
        }
    }

    private static void addTicketsCLI(Long eventId, int quantity) {
        try {
            URL url = new URL("http://localhost:8080/tickets/addTickets?eventId=" + eventId + "&quantity=" + quantity);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Tickets added successfully.");
            } else {
                System.out.println("Failed to add tickets.");
            }
        } catch (Exception e) {
            System.out.println("Error adding tickets: " + e.getMessage());
        }
    }

    private static void buyTicketsCLI(Long eventId, int quantity) {
        try {
            URL url = new URL("http://localhost:8080/tickets/buy?eventId=" + eventId + "&quantity=" + quantity);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Tickets purchased successfully.");
            } else {
                System.out.println("Failed to purchase tickets.");
            }
        } catch (Exception e) {
            System.out.println("Error buying tickets: " + e.getMessage());
        }
    }


}






//package com.shavi.realtimeeventticketingsystemcli;
//
//import com.shavi.realtimeeventticketingsystemcli.configurations.LoggerConfiguration;
//import com.shavi.realtimeeventticketingsystemcli.configurations.SystemConfiguration;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.Scanner;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.io.OutputStream;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.atomic.AtomicBoolean;
//
//public class Main extends LoggerConfiguration {
//
//    private static final AtomicBoolean isRunning = new AtomicBoolean(false);
//    private static ExecutorService vendorService;
//    private static ExecutorService customerService;
//    private static volatile boolean isMonitoring = true;  // Flag to control monitoring thread
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
//        TicketPool ticketPool = new TicketPool(maxTicketCapacity);
//
//        while (true) {
//            if (!isRunning.get()) { // Only prompt for input if ticket handling is not running
//                System.out.println("Enter '1' to START ticket handling, '2' to PAUSE,");
//                System.out.println("'3' to ADD tickets, '4' to BUY tickets, '5' to MONITOR ticket pool, or '0' to EXIT:");
//            }
//
//            String command = scanner.nextLine().trim().toLowerCase();
//
//            if ("1".equals(command)) {
//                if (isRunning.compareAndSet(false, true)) {
//                    System.out.println("Starting ticket handling operations...");
//                    vendorService = Executors.newFixedThreadPool(3); // Reinitialize vendorService
//                    customerService = Executors.newFixedThreadPool(5); // Reinitialize customerService
//                    startTicketingOperations(vendorService, customerService, ticketPool, ticketReleaseRate, totalTickets);
//                } else {
//                    System.out.println("Ticket handling is already running.");
//                }
//            } else if ("2".equals(command)) {
//                if (isRunning.compareAndSet(true, false)) {
//                    System.out.println("Stopping ticket handling operations...");
//                    stopTicketingOperations(vendorService, customerService);
//                } else {
//                    System.out.println("Ticket handling is not currently running.");
//                }
//            } else if ("3".equals(command)) { // Add tickets
//                System.out.print("Enter Event ID to add tickets to: ");
//                Long eventId = scanner.nextLong();
//                System.out.print("Enter number of tickets to add: ");
//                int quantity = scanner.nextInt();
//                scanner.nextLine(); // Consume the leftover newline character
//                addTicketsCLI(eventId, quantity);
//            } else if ("4".equals(command)) { // Buy tickets
//                System.out.print("Enter Event ID to buy tickets for: ");
//                Long eventId = scanner.nextLong();
//                System.out.print("Enter number of tickets to buy: ");
//                int quantity = scanner.nextInt();
//                scanner.nextLine(); // Consume the leftover newline character
//                buyTicketsCLI(eventId, quantity);
//            } else if ("5".equals(command)) { // Monitor ticket pool
//                System.out.print("Enter Event ID to monitor: ");
//                Long eventId = scanner.nextLong();
//                scanner.nextLine(); // Consume the leftover newline character
//                new Thread(() -> monitorTicketPool(eventId)).start();
//                System.out.println("Monitoring started for Event ID: " + eventId);
//            } else if ("0".equals(command)) {
//                System.out.println("Exiting the system. Goodbye!");
//                logger.info("Exiting the Ticketing System CLI");
//                stopTicketingOperations(vendorService, customerService);
//                isMonitoring = false;  // Stop the monitoring thread
//                break;
//            } else {
//                System.out.println("Invalid command! Please enter '1', '2', '3', '4', '5', or '0'.");
//            }
//        }
//        scanner.close();
//    }
//
//    private static void startTicketingOperations(ExecutorService vendorService, ExecutorService customerService, TicketPool ticketPool, int ticketReleaseRate, int totalTickets) {
//        // Start vendor threads for concurrent ticket releasing
//        for (int i = 1; i <= 3; i++) {  // Create 3 vendors as an example
//            Vendor vendor = new Vendor(i, ticketReleaseRate, 1000, ticketPool, totalTickets);  // Release tickets every 2 seconds
//            vendorService.execute(vendor);
//        }
//
//        // Start customer threads for purchasing tickets
//        for (int i = 1; i <= 5; i++) {  // Create 5 customers as an example
//            int ticketsPerRetrieval = 2; // Each customer attempts to retrieve 2 tickets at a time
//            Customer customer = new Customer(i, ticketPool, 2000, ticketsPerRetrieval);
//            customerService.execute(customer);
//        }
//
//        // Start monitoring the ticketing system
//        new Thread(() -> monitorTicketingOperations(ticketPool)).start();
//    }
//
//    private static void monitorTicketingOperations(TicketPool ticketPool) {
//        while (isMonitoring) {
//            try {
//                int currentSize = ticketPool.getCurrentSize(); // Assuming TicketPool has a method to get the current size
//                logger.info("Monitoring Ticket Pool: Current Size = " + currentSize);
//                System.out.println("Monitoring Ticket Pool: Current Size = " + currentSize);
//
//                // Check for specific conditions, such as if the pool is empty
//                if (currentSize == 0) {
//                    logger.warning("Warning: Ticket pool is empty!");
//                    System.out.println("Warning: Ticket pool is empty!");
//                    // Optionally, pause ticket handling if desired (e.g., stop vendors/consumers)
//                }
//
//                Thread.sleep(5000); // Monitor every 5 seconds
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                logger.warning("Monitoring thread interrupted.");
//                break;
//            }
//        }
//    }
//
//
//    private static void stopTicketingOperations(ExecutorService vendorService, ExecutorService customerService) {
//        // Attempt to stop the services gracefully
//        if (vendorService != null && !vendorService.isShutdown()) {
//            vendorService.shutdownNow();
//        }
//        if (customerService != null && !customerService.isShutdown()) {
//            customerService.shutdownNow();
//        }
//
//        try {
//            if (vendorService != null && !vendorService.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
//                vendorService.shutdownNow();
//            }
//            if (customerService != null && !customerService.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
//                customerService.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            if (vendorService != null) vendorService.shutdownNow();
//            if (customerService != null) customerService.shutdownNow();
//            logger.severe("Error during shutdown of services: " + e.getMessage());
//        }
//    }
//
//    private static void monitorTicketPool(Long eventId) {
//        while (isMonitoring) {
//            try {
//                URL url = new URL("http://localhost:8080/tickets/availability?eventId=" + eventId);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//
//                int availableTickets;
//                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//                    availableTickets = Integer.parseInt(reader.readLine());
//                }
//
//                System.out.println("Monitoring Ticket Pool: Available Tickets = " + availableTickets);
//                if (availableTickets == 0) {
//                    System.out.println("Warning: No tickets available for this event!");
//                }
//
//                Thread.sleep(5000); // Check every 5 seconds
//            } catch (Exception e) {
//                System.out.println("Error monitoring ticket pool: " + e.getMessage());
//            }
//        }
//    }
//
//    private static void addTicketsCLI(Long eventId, int quantity) {
//        try {
//            URL url = new URL("http://localhost:8080/tickets/addTickets?eventId=" + eventId + "&quantity=" + quantity);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                System.out.println("Tickets added successfully.");
//            } else {
//                System.out.println("Failed to add tickets.");
//            }
//        } catch (Exception e) {
//            System.out.println("Error adding tickets: " + e.getMessage());
//        }
//    }
//
//    private static void buyTicketsCLI(Long eventId, int quantity) {
//        try {
//            URL url = new URL("http://localhost:8080/tickets/buy?eventId=" + eventId + "&quantity=" + quantity);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                System.out.println("Tickets purchased successfully.");
//            } else {
//                System.out.println("Failed to purchase tickets.");
//            }
//        } catch (Exception e) {
//            System.out.println("Error buying tickets: " + e.getMessage());
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
//                    System.out.printf("Invalid input! Please enter a number between %d and %d%n", min, max);
//                    logger.warning("Invalid input! Number should be between " + min + " and " + max);
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
//            OutputStream os = connection.getOutputStream();
//            os.write(configData.getBytes());
//            os.flush();
//            os.close();
//
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
//            logger.severe("Connection refused. Could not connect to the server at localhost:8080.");
//            System.out.println("Unable to connect to the server. Please check if the backend is running and try again.");
//        } catch (java.io.IOException e) {
//            logger.severe("Error occurred while sending configuration to backend: " + e.getMessage());
//            System.out.println("There was an error sending data to the server. Please check your network connection and try again.");
//        } catch (Exception e) {
//            logger.severe("Unexpected error occurred: " + e.getMessage());
//            System.out.println("An unexpected error occurred. Please try again later.");
//        }
//    }
//}
