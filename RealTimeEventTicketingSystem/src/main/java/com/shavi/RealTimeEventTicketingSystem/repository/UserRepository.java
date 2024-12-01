package com.shavi.RealTimeEventTicketingSystem.repository;

import com.shavi.RealTimeEventTicketingSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);
    List<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
}
