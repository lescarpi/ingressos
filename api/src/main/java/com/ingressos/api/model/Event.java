package com.ingressos.api.model;

import com.ingressos.api.http.request.UpdateEventRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(schema = "events", name = "events")
@Entity(name = "Event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String location;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime date;

    public void update(UpdateEventRequest request) {
        this.name = request.name();
        this.description = request.description();
        this.location = request.location();
        this.date = request.date();
    }

}
