package com.shavi.RealTimeEventTicketingSystem.repository;

import com.shavi.RealTimeEventTicketingSystem.entity.SystemConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemConfigurationRepository extends JpaRepository<SystemConfiguration, Long> {
}
