package com.shavi.RealTimeEventTicketingSystem.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventRequest {
    private Long eventId;
    private String eventName;
    private int noOfTickets;
    private LocalDate date;
    private Integer userId;
}
