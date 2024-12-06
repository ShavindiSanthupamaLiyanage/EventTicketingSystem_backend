package com.shavi.RealTimeEventTicketingSystem.entity;

import com.shavi.RealTimeEventTicketingSystem.enums.EventCategory;
import com.shavi.RealTimeEventTicketingSystem.enums.EventStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;
    private String eventName;
    private int ticketPrice;
    private int noOfTickets;
    private int totalTickets;
    private LocalDate date;
    private String venue;
    private String description;
    private EventCategory category;
    private EventStatus status;

    
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "user_id", nullable = false)
    private Integer userId;
}
