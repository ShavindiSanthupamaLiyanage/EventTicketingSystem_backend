package com.shavi.realtimeeventticketingsystemcli;

import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends LoggerConfiguration {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Ticketing System CLI");
        logger.info("Starting the Ticketing System CLI");

        int totalTickets = getValidInput(scanner, "Enter total tickets (0 or positive):", 0, Integer.MAX_VALUE);
        int ticketReleaseRate = getValidInput(scanner, "Enter ticket release rate (0 to 100):", 0, 100);
        int customerRetrievalRate = getValidInput(scanner, "Enter customer retrieval rate (1 to 10):", 1, 10);
        int maxTicketCapacity = getValidInput(scanner, "Enter maximum ticket capacity (0 or positive):", 0, Integer.MAX_VALUE);

        // Create a SystemConfiguration object
        SystemConfiguration config = new SystemConfiguration(totalTickets, maxTicketCapacity, customerRetrievalRate, ticketReleaseRate);

        // Send data to backend
        sendConfigToBackend(config);

        // Set up TicketPool with maxTicketCapacity
        TicketPool ticketPool = new TicketPool(maxTicketCapacity);

        // Start vendor threads for concurrent ticket releasing
        ExecutorService vendorService = Executors.newFixedThreadPool(3); // Adjust the number of vendors as needed
        for (int i = 1; i <= 3; i++) {  // Create 3 vendors as an example
            Vendor vendor = new Vendor(i, ticketReleaseRate, 2000, ticketPool);  // Release tickets every 2 seconds
            vendorService.execute(vendor);
        }

        // Start customer threads for purchasing tickets
        ExecutorService customerService = Executors.newFixedThreadPool(5); // Adjust the number of customers as needed
        for (int i = 1; i <= 5; i++) {  // Create 5 customers as an example
            int retrievalInterval = 1000; // 1 second interval for each customer
            int ticketsPerRetrieval = 2; // Each customer attempts to retrieve 2 tickets at a time
            Customer customer = new Customer(i, ticketPool, retrievalInterval, ticketsPerRetrieval);
            customerService.execute(customer);
        }

        // Optionally shutdown services after some time to stop all vendors and customers (e.g., after 30 seconds)
        vendorService.shutdown();
        customerService.shutdown();
        // Shutdown executor after some time to stop all vendors (e.g., after 10 seconds)
        //executorService.shutdown();

        // Await termination if desired
        try {
            if (!vendorService.awaitTermination(30, java.util.concurrent.TimeUnit.SECONDS)) {
                vendorService.shutdownNow(); // Force shutdown if not terminated
            }
            if (!customerService.awaitTermination(30, java.util.concurrent.TimeUnit.SECONDS)) {
                customerService.shutdownNow(); // Force shutdown if not terminated
            }
        } catch (InterruptedException e) {
            vendorService.shutdownNow();
            customerService.shutdownNow();
            logger.severe("Error during termination of service: " + e.getMessage());
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
            }
        } catch (Exception e) {
            logger.severe("Error occurred while sending configuration to backend: " + e.getMessage());
            e.printStackTrace();
        }
    }
}