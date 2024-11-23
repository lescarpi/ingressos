package com.ingressos.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
@Table(name = "tickets")
@Entity(name = "Ticket")
public class Ticket {

    public Ticket(Event event, BigDecimal price, String seat) {
        this.event = event;
        this.price = price;
        this.status = Status.AVAILABLE;
        this.seat = seat;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String seat;

}
