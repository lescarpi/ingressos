package com.ingressos.api.http.request;

import java.time.LocalDateTime;

public record UpdateEventRequest(
        String name,
        String description,
        String location,
        LocalDateTime date
) {
}
