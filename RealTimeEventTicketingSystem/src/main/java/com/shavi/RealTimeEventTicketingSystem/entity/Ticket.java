package com.shavi.RealTimeEventTicketingSystem.entity;

import com.shavi.RealTimeEventTicketingSystem.enums.TicketStatus;
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
    private Long eventId;
    private Integer userId;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;


}
