package com.ingressos.api.controller.interfaces;

import com.ingressos.api.http.request.BookTicketRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("ticket")
public interface TicketControllerInterface {

    @PostMapping("/book")
    public ResponseEntity<?> book(@RequestBody BookTicketRequest request);

    // public ResponseEntity<?> purchase(@RequestBody String)

}
