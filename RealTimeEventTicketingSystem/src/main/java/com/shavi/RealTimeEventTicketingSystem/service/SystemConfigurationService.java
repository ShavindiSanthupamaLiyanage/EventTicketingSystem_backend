package com.shavi.RealTimeEventTicketingSystem.service;

import com.shavi.RealTimeEventTicketingSystem.entity.SystemConfiguration;
import com.shavi.RealTimeEventTicketingSystem.repository.SystemConfigurationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemConfigurationService {
    private final SystemConfigurationRepository repository;

    public SystemConfigurationService(SystemConfigurationRepository repository) {
        this.repository = repository;
    }

    public SystemConfiguration saveConfiguration(SystemConfiguration config) {
        if (getRunningConfiguration() != null && getRunningConfiguration().isRunning()) {
            throw new IllegalStateException("System is running. Stop it before reconfiguring.");
        }
        return repository.save(config);
    }

    public SystemConfiguration getConfiguration(Long id) {
        return repository.findById(id).orElse(null);
    }

    public SystemConfiguration startSystem(Long id) {
        SystemConfiguration config = getConfiguration(id);
        if (config == null) {
            throw new IllegalArgumentException("Configuration not found.");
        }
        config.setRunning(true);
        return repository.save(config);
    }

    public SystemConfiguration stopSystem(Long id) {
        SystemConfiguration config = getConfiguration(id);
        if (config == null) {
            throw new IllegalArgumentException("Configuration not found.");
        }
        config.setRunning(false);
        return repository.save(config);
    }

    public SystemConfiguration getRunningConfiguration() {
        return repository.findAll()
                .stream()
                .filter(SystemConfiguration::isRunning)
                .findFirst()
                .orElse(null);
    }

    public List<SystemConfiguration> getAllConfigurations() {
        return repository.findAll();
    }
}
