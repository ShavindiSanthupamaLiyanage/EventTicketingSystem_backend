package com.shavi.RealTimeEventTicketingSystem.dto.request;

import com.shavi.RealTimeEventTicketingSystem.enums.EventCategory;
import com.shavi.RealTimeEventTicketingSystem.enums.EventStatus;
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
    private int ticketPrice;
    private int noOfTickets;
    private int totalTickets;
    private LocalDate date;
    private Integer userId;
    private String venue;
    private String description;
    private EventCategory category;
    private EventStatus status;
}
