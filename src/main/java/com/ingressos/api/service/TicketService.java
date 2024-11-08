package com.ingressos.api.service;

import com.ingressos.api.http.request.BookTicketRequest;
import com.ingressos.api.model.Reservation;
import com.ingressos.api.model.Status;
import com.ingressos.api.model.Ticket;
import com.ingressos.api.repository.ReservationRepository;
import com.ingressos.api.repository.TicketRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    private final ReservationRepository reservationRepository;

    private final RedisTemplate<String, Reservation> redisTemplate;

    public TicketService(TicketRepository ticketRepository, ReservationRepository reservationRepository, RedisTemplate<String, Reservation> redisTemplate) {
        this.ticketRepository = ticketRepository;
        this.reservationRepository = reservationRepository;
        this.redisTemplate = redisTemplate;
    }

    public ResponseEntity<?> book(BookTicketRequest request) {
        List<Long> ticketIds = request.ticketIds();

        if (ticketIds.isEmpty()) {
            return new ResponseEntity<>("Pelo menos um ticket precisa ser reservado!", HttpStatus.BAD_REQUEST);
        }

        List<Ticket> foundTickets = ticketRepository.findAllByIds(ticketIds);
        List<Long> foundTicketsIds = foundTickets.stream()
                .map(Ticket::getId)
                .toList();

        List<Long> notFoundTicketsIds = ticketIds.stream()
                .filter(id -> !foundTicketsIds.contains(id))
                .toList();

        if (!notFoundTicketsIds.isEmpty()) {
            String message = "Os seguintes tickets não foram encontrados: " +
                    notFoundTicketsIds.stream()
                            .map(String::valueOf).collect(Collectors.joining(", ")) + "!";
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        List<Long> notAvailableTicketsIds = foundTickets.stream()
                .filter(ticket -> ticket.getStatus() != Status.AVAILABLE)
                .map(Ticket::getId)
                .toList();

        if (!notAvailableTicketsIds.isEmpty()) {
            String message = "Os seguintes tickets não estão disponíveis: " +
                    notAvailableTicketsIds.stream()
                            .map(String::valueOf).collect(Collectors.joining(", ")) + "!";
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        makeReservationWithTTL(ticketIds);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void makeReservationWithTTL(List<Long> ticketIds) {
        long ttlSeconds = 900; // 15 min

        List<Reservation> reservations = ticketIds.stream()
                .map(Reservation::new)
                .toList();

        reservations.forEach(reservation -> {
            reservationRepository.save(reservation);
            redisTemplate.expire(reservation.getId(), ttlSeconds, TimeUnit.SECONDS);
        });
    }

}
