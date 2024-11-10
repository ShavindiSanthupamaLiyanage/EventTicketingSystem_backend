package com.shavi.RealTimeEventTicketingSystem.controller;

import com.shavi.RealTimeEventTicketingSystem.entity.Configuration;
import com.shavi.RealTimeEventTicketingSystem.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliConfiguration")
public class ConfigurationController {
    @Autowired
    private ConfigurationService configurationService;

    @PostMapping("/post/configure")
    public Configuration configureSystem(@RequestBody Configuration configuration) {
        return configurationService.saveConfiguration(configuration);
    }

    @GetMapping("/get/configure")
    public List<Configuration> getConfiguration() {
        return configurationService.getAllConfigurations();
    }

    @GetMapping("/get/configure/{id}")
    public Configuration getConfigurationById(@PathVariable Long id) {
        return configurationService.getConfigurationById(id);
    }

}

