package com.ingressos.api.controller.interfaces;

import com.ingressos.api.http.request.CreateEventRequest;
import com.ingressos.api.http.request.UpdateEventRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequestMapping("event")
public interface EventControllerInterface {

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody CreateEventRequest request);

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateEventRequest request);

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id);

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam int page,
                                    @RequestParam int size,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) LocalDateTime startDate,
                                    @RequestParam(required = false) LocalDateTime endDate);

}
