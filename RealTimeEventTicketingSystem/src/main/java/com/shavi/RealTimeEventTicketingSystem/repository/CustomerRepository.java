package com.shavi.RealTimeEventTicketingSystem.repository;

import com.shavi.RealTimeEventTicketingSystem.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
