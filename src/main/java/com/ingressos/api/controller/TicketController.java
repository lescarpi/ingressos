package com.ingressos.api.controller;

import com.ingressos.api.controller.interfaces.TicketControllerInterface;
import com.ingressos.api.http.request.BookTicketRequest;
import com.ingressos.api.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketController implements TicketControllerInterface {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<?> book(BookTicketRequest request) {
        return service.book(request);
    }

}
