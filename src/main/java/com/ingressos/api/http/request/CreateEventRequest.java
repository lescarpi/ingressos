package com.ingressos.api.http.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateEventRequest(
        String name,
        String description,
        String location,
        LocalDateTime date,
        BigDecimal ticketPrice,
        Integer ticketAmount
) {
}
