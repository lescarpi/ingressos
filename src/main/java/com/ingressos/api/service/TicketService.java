package com.ingressos.api.service;

import com.ingressos.api.http.request.BookTicketRequest;
import com.ingressos.api.http.request.PurchaseTicketRequest;
import com.ingressos.api.http.response.TicketDetails;
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
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        List<Long> reservedTickets = findReservedTickets(ticketIds);
        if (!reservedTickets.isEmpty()) {
            String message = "Os seguintes tickets já estão reservados: " +
                    reservedTickets.stream()
                            .map(String::valueOf).collect(Collectors.joining(", ")) + "!";
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        makeReservationWithTTL(ticketIds);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> purchase(PurchaseTicketRequest request) {
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
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }

        foundTickets.forEach(ticket -> ticket.setStatus(Status.SOLD));
        ticketRepository.saveAll(foundTickets);

        List<TicketDetails> ticketDetails = foundTickets.stream()
                .map(TicketDetails::new)
                .toList();

        return new ResponseEntity<>(ticketDetails, HttpStatus.OK);
    }

    private List<Long> findReservedTickets(List<Long> ticketIds) {
        return ticketIds.stream()
                .filter(ticketId -> {
                    String redisKey = "reservation:ticketId:" + ticketId;
                    return Boolean.TRUE.equals(redisTemplate.hasKey(redisKey));
                })
                .toList();
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
