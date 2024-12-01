package com.shavi.RealTimeEventTicketingSystem.repository;

import com.shavi.RealTimeEventTicketingSystem.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByEventName(String eventName);
}
