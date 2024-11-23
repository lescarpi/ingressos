package com.ingressos.api.service;

import com.ingressos.api.http.request.CreateEventRequest;
import com.ingressos.api.http.response.EventDetails;
import com.ingressos.api.http.request.UpdateEventRequest;
import com.ingressos.api.model.Event;
import com.ingressos.api.model.Ticket;
import com.ingressos.api.repository.EventRepository;
import com.ingressos.api.repository.TicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    private final TicketRepository ticketRepository;

    private final PagedResourcesAssembler<EventDetails> pagedResourcesAssembler;

    public EventService(EventRepository eventRepository, TicketRepository ticketRepository, PagedResourcesAssembler<EventDetails> pagedResourcesAssembler) {
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Transactional
    public ResponseEntity<?> create(CreateEventRequest request) {
        Event event = Event.builder()
                .name(request.name())
                .description(request.description())
                .location(request.location())
                .date(request.date())
                .build();
        event = eventRepository.save(event);

        List<Ticket> tickets = createTickets(event, request.ticketPrice(), request.ticketAmount());
        ticketRepository.saveAll(tickets);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<?> update(Long id, UpdateEventRequest request) {
        Event event = eventRepository.findById(id)
                .orElse(null);

        if (event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        event.update(request);

        eventRepository.save(event);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> findById(Long id) {
        Event event = eventRepository.findById(id)
                .orElse(null);

        if (event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.status(HttpStatus.OK).body(new EventDetails(event));
    }

    public ResponseEntity<?> search(int page, int size, String name, LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null) {
            startDate = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        }

        if (endDate == null) {
            endDate = LocalDateTime.of(2030, 12, 31, 23, 59, 59);
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<EventDetails> events = eventRepository.search(name, startDate, endDate, pageable).map(EventDetails::new);

        PagedModel<EntityModel<EventDetails>> pagedModel = pagedResourcesAssembler.toModel(events);

        return ResponseEntity.status(HttpStatus.OK).body(pagedModel);
    }

    private List<Ticket> createTickets(Event event, BigDecimal ticketPrice, Integer ticketAmount) {
        String[] seatCodes = generateSeats(ticketAmount);

        List<Ticket> tickets = new ArrayList<Ticket>();
        for (int i = 0; i < ticketAmount; i++) {
            tickets.add(new Ticket(event, ticketPrice, seatCodes[i]));
        }
        return tickets;
    }

    // Função que gera um código para cada assento no formato A1, B1, C1 ...
    // Apenas representativo, não importa para o funcionamento da aplicação
    private String[] generateSeats(Integer ticketAmount) {
        String[] codes = new String[ticketAmount];
        int codeCount = 0;

        int lettersCount = 26; // Quantidade de letras de A a Z
        int numbersPerLetter = (int) Math.ceil((double) ticketAmount / lettersCount); // Arredonda para cima

        for (int number = 1; number <= numbersPerLetter; number++) {
            for (char letter = 'A'; letter <= 'Z' && codeCount < ticketAmount; letter++) {
                codes[codeCount++] = letter + String.valueOf(number);
            }
        }

        return codes;
    }

}
