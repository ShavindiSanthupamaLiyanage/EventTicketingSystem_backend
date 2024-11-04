package com.shavi.RealTimeEventTicketingSystem;

import com.shavi.RealTimeEventTicketingSystem.entity.Configuration;
import com.shavi.RealTimeEventTicketingSystem.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartupConfigLoader {

    @Autowired
    private ConfigurationService configurationService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent() {
        List<Configuration> config = configurationService.getAllConfigurations();
        if (config != null) {
            // Use config values to initialize components as needed
            System.out.println("Configuration loaded: " + config.toString());
        } else {
            System.out.println("No configuration found. Please configure the system.");
        }
    }
}

