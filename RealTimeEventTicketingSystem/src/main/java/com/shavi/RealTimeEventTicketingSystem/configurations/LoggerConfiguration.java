package com.shavi.RealTimeEventTicketingSystem.configurations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class LoggerConfiguration {
    protected final Logger logger = LogManager.getLogger(this.getClass());
}