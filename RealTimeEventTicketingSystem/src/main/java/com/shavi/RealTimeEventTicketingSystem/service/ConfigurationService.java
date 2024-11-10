package com.shavi.RealTimeEventTicketingSystem.service;

import com.shavi.RealTimeEventTicketingSystem.entity.Configuration;
import com.shavi.RealTimeEventTicketingSystem.exception.ResourceNotFoundException;
import com.shavi.RealTimeEventTicketingSystem.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigurationService {
    @Autowired
    private ConfigurationRepository repository;

    public Configuration saveConfiguration(Configuration config) {
        return repository.save(config);
    }

//    public Configuration getCurrentConfiguration() {
//        return repository.findAll().stream()
//                .findFirst()
//                .orElse(null);
//    }

    public List<Configuration> getAllConfigurations() {
        return repository.findAll();
    }

    public Configuration getConfigurationById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Configuration not found with id " + id));
    }
}
