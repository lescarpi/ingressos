package com.ingressos.api.http.response;

import com.ingressos.api.model.Status;
import com.ingressos.api.model.Ticket;

import java.math.BigDecimal;

public record TicketDetails(
        Long id,
        BigDecimal price,
        Status status,
        String seat,
        EventDetails eventDetails) {

    public TicketDetails(Ticket ticket) {
        this(ticket.getId(), ticket.getPrice(), ticket.getStatus(), ticket.getSeat(), new EventDetails(ticket.getEvent()));
    }

}
