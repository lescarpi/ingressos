package com.ingressos.api.model;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("reservation")
public class Reservation {

    @Id
    private String id;

    private boolean booked;

    public Reservation(Long ticketId) {
        this.id = "ticketId:" + ticketId;
        this.booked = true;
    }

}
