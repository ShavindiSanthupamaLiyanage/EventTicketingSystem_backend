package com.shavi.RealTimeEventTicketingSystem.repository;


import com.shavi.RealTimeEventTicketingSystem.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;



@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t WHERE t.status = 'AVAILABLE' AND t.eventId = :eventId ORDER BY t.id ASC")
    List<Ticket> findTopNAvailableTickets(@Param("eventId") Long eventId, Pageable pageable);

}

