//package com.shavi.realtimeeventticketingsystemcli;
//
//import com.shavi.realtimeeventticketingsystemcli.configurations.LoggerConfiguration;
//import com.shavi.realtimeeventticketingsystemcli.configurations.SystemConfiguration;
//
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class BackendService extends LoggerConfiguration {
//
//    public void sendConfigToBackend(SystemConfiguration config) {
//        String configData = String.format(
//                "{\"totalTickets\": %d, \"ticketReleaseRate\": %d, \"customerRetrievalRate\": %d, \"maxTicketCapacity\": %d}",
//                config.getTotalTickets(), config.getTicketReleaseRate(),
//                config.getCustomerRetrievalRate(), config.getMaxTicketCapacity());
//
//        try {
//            URL url = new URL("http://localhost:8080/api/cliConfiguration/post/configure");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setDoOutput(true);
//
//            try (OutputStream os = connection.getOutputStream()) {
//                os.write(configData.getBytes());
//            }
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                logger.info("Configuration saved successfully.");
//            } else {
//                logger.warning("Failed to save configuration. Response Code: " + responseCode);
//            }
//        } catch (Exception e) {
//            logger.severe("Error occurred while sending configuration: " + e.getMessage());
//        }
//    }
//}
