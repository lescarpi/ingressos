package com.ingressos.api.controller;

import com.ingressos.api.http.request.CreateEventRequest;
import com.ingressos.api.http.request.UpdateEventRequest;
import com.ingressos.api.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class EventController implements EventControllerInterface {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<?> create(CreateEventRequest request) {
        return service.create(request);
    }

    @Override
    public ResponseEntity<?> update(Long id, UpdateEventRequest request) {
        return service.update(id, request);
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        return service.findById(id);
    }

    @Override
    public ResponseEntity<?> search(int page, int size, String name, LocalDateTime startDate, LocalDateTime endDate) {
        return service.search(page, size, name, startDate, endDate);
    }

}
