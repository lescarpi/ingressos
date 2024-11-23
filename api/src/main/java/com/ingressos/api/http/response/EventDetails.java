package com.ingressos.api.http.response;

import com.ingressos.api.model.Event;

import java.time.LocalDateTime;

public record EventDetails(
        String name,
        String description,
        String location,
        LocalDateTime date
) {

    public EventDetails(Event event) {
        this(event.getName(), event.getDescription(), event.getLocation(), event.getDate());
    }

}
