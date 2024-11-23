package com.ingressos.api.repository;

import com.ingressos.api.model.Reservation;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends KeyValueRepository<Reservation, String> {
}
