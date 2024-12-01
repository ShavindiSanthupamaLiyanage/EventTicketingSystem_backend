package com.shavi.RealTimeEventTicketingSystem.controller;

import com.shavi.RealTimeEventTicketingSystem.entity.SystemConfiguration;
import com.shavi.RealTimeEventTicketingSystem.service.SystemConfigurationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/configurations")
public class SystemConfigurationController {
    private final SystemConfigurationService service;

    public SystemConfigurationController(SystemConfigurationService service) {
        this.service = service;
    }

    @PostMapping
    public SystemConfiguration saveConfig(@RequestBody SystemConfiguration config) {
        return service.saveConfiguration(config);
    }

    @GetMapping("/{id}")
    public SystemConfiguration getConfig(@PathVariable Long id) {
        return service.getConfiguration(id);
    }

    @PostMapping("/{id}/start")
    public SystemConfiguration startSystem(@PathVariable Long id) {
        return service.startSystem(id);
    }

    @PostMapping("/{id}/stop")
    public SystemConfiguration stopSystem(@PathVariable Long id) {
        return service.stopSystem(id);
    }
}
