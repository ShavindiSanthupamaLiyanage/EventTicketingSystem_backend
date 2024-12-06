package com.shavi.RealTimeEventTicketingSystem.controller;

import com.shavi.RealTimeEventTicketingSystem.entity.SystemConfiguration;
import com.shavi.RealTimeEventTicketingSystem.service.SystemConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/configurations")
public class SystemConfigurationController {

    @Autowired
    private SystemConfigurationService systemConfigurationService;

    @PostMapping("/add")
    public SystemConfiguration saveConfig(@RequestBody SystemConfiguration config) {
        return systemConfigurationService.saveConfiguration(config);
    }

    @GetMapping("/{id}")
    public SystemConfiguration getConfig(@PathVariable Long id) {
        return systemConfigurationService.getConfiguration(id);
    }

    @PostMapping("/{id}/start")
    public SystemConfiguration startSystem(@PathVariable Long id) {
        return systemConfigurationService.startSystem(id);
    }

    @PostMapping("/{id}/stop")
    public SystemConfiguration stopSystem(@PathVariable Long id) {
        return systemConfigurationService.stopSystem(id);
    }

    @GetMapping("/all")
    public List<SystemConfiguration> getAllConfigurations() {
        return systemConfigurationService.getAllConfigurations();
    }
}
