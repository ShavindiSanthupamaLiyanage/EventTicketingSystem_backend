package com.shavi.RealTimeEventTicketingSystem.service;

import com.shavi.RealTimeEventTicketingSystem.configurations.LoggerConfiguration;
import com.shavi.RealTimeEventTicketingSystem.entity.SystemConfiguration;
import com.shavi.RealTimeEventTicketingSystem.repository.SystemConfigurationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemConfigurationService extends LoggerConfiguration {
    private final SystemConfigurationRepository repository;

    public SystemConfigurationService(SystemConfigurationRepository repository) {
        this.repository = repository;
    }

    public SystemConfiguration saveConfiguration(SystemConfiguration config) {
        logger.info("Received request to save system configuration: {}", config);

        // Check if system is running before allowing configuration changes
        if (getRunningConfiguration() != null && getRunningConfiguration().isRunning()) {
            logger.error("Cannot reconfigure: System is already running.");
            throw new IllegalStateException("System is running. Stop it before reconfiguring.");
        }
        SystemConfiguration savedConfig = repository.save(config);
        logger.info("System configuration saved successfully with ID: {}", savedConfig.getId());

        return savedConfig;
    }

    public SystemConfiguration getConfiguration(Long id) {
        logger.info("Fetching system configuration with ID: {}", id);
        return repository.findById(id).orElse(null);
    }

    public SystemConfiguration startSystem(Long id) {
        logger.info("Received request to start system with configuration ID: {}", id);

        SystemConfiguration config = getConfiguration(id);
        if (config == null) {
            logger.error("Configuration with ID {} not found. Cannot start system.", id);
            throw new IllegalArgumentException("Configuration not found.");
        }
        config.setRunning(true);
        logger.info("System with configuration ID {} started successfully.", id);
        return repository.save(config);
    }

    public SystemConfiguration stopSystem(Long id) {
        logger.info("Received request to stop system with configuration ID: {}", id);

        SystemConfiguration config = getConfiguration(id);
        if (config == null) {
            logger.error("Configuration with ID {} not found. Cannot stop system.", id);
            throw new IllegalArgumentException("Configuration not found.");
        }
        config.setRunning(false);
        logger.info("System with configuration ID {} stopped successfully.", id);
        return repository.save(config);
    }

    public SystemConfiguration getRunningConfiguration() {
        logger.info("Fetching running system configuration.");

        return repository.findAll()
                .stream()
                .filter(SystemConfiguration::isRunning)
                .findFirst()
                .orElse(null);

    }

    public List<SystemConfiguration> getAllConfigurations() {
        logger.info("Fetching all system configurations.");
        return repository.findAll();
    }
}
