package com.shavi.RealTimeEventTicketingSystem.entity;

//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDate;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@Entity
//public class Ticket {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "event_id", nullable = false)
//    private Event event;
//
////    @ManyToOne
////    @JoinColumn(name = "customer_id", nullable = false)
////    private Customer customer;
//
//    @Column(nullable = false)
//    private Integer quantity;
//
//    @Column(nullable = false)
//    private LocalDate purchaseDate;
//
//    @Column(nullable = false)
//    private Integer price;
//}


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "eventId", insertable = false, updatable = false)
    private Event event;  // This will be used for reference only, the event_id will be stored in the database

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;  // This will be used for reference only, the user_id will be stored in the database

    @Column(name = "event_id")
    private Long eventId;  // Store only the event_id as a foreign key

    @Column(name = "user_id")
    private Integer userId;  // Store only the user_id as a foreign key

    private int quantity;
    private String status;  // E.g., PURCHASED, RESERVED, etc.

}
