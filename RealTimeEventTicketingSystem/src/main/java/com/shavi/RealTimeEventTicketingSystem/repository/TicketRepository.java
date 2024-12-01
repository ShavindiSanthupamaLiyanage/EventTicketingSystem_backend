package com.shavi.RealTimeEventTicketingSystem.repository;

//import com.shavi.RealTimeEventTicketingSystem.entity.Ticket;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface TicketRepository extends JpaRepository<Ticket, Long> {
//
//    @Query("SELECT COALESCE(SUM(et.quantity), 0) FROM Ticket et WHERE et.event.id = :eventId")
//    int sumTicketsByEvent(Long eventId);
//}


import com.shavi.RealTimeEventTicketingSystem.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
}
