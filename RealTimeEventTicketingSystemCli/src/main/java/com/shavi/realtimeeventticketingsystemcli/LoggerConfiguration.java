package com.shavi.realtimeeventticketingsystemcli;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerConfiguration {
    protected static final Logger logger = Logger.getLogger(LoggerConfiguration.class.getName());

    static {
        try {
            // Generate a timestamped log file name
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String logFileName = "ticketing_system_cli_logs_" + timestamp + ".log";


            // Set up the file handler for logging
            FileHandler fileHandler = new FileHandler(logFileName, true);
            fileHandler.setFormatter(new SimpleFormatter()); // Optional: set a formatter
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false); // Disable console logging if desired
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
