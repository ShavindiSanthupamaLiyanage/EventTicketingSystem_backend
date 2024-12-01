//package com.shavi.RealTimeEventTicketingSystem.entity;

//import com.shavi.RealTimeEventTicketingSystem.enums.EventCategory;
//import com.shavi.RealTimeEventTicketingSystem.enums.EventStatus;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@Entity
//public class Event {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "event_id")
//    private Long id;
//
//    @Column(nullable = false)
//    private String name;
//
//    private String description;
//
//    @Enumerated(EnumType.STRING)
//    private EventCategory category;
//
//    @Column(nullable = false)
//    private LocalDate date;
//
//    @Column(nullable = false)
//    private String location;
//
////    @ManyToOne
////    @JoinColumn(name = "vendor_id", nullable = false)
////    private Vendor vendor;
//
//    @Column(nullable = false)
//    private Integer totalTickets;
//
//    @Column(nullable = false)
//    private Integer availableTickets;
//
//    @Column(nullable = false)
//    private Integer soldTickets = 0;
//
//    @Column(nullable = false)
//    private Integer ticketPrice;
//
//    @Enumerated(EnumType.STRING)
//    private EventStatus status;
//}


package com.shavi.RealTimeEventTicketingSystem.entity;

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
    private int noOfTickets;
    private LocalDate date;
    
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "user_id", nullable = false)  // Only store the userId
    private Integer userId;  // This is the ID of the vendor who created the event
}
