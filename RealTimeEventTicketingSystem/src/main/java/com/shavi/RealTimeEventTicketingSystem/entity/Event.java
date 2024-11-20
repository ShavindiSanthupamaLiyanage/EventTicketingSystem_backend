package com.shavi.RealTimeEventTicketingSystem.entity;

import com.shavi.RealTimeEventTicketingSystem.enums.EventCategory;
import com.shavi.RealTimeEventTicketingSystem.enums.EventStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private EventCategory category;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String location;

    @ManyToOne
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(nullable = false)
    private Integer totalTickets;

    @Column(nullable = false)
    private Integer availableTickets;

    @Column(nullable = false)
    private Integer soldTickets = 0;

    @Column(nullable = false)
    private Integer ticketPrice;

    @Enumerated(EnumType.STRING)
    private EventStatus status;
}