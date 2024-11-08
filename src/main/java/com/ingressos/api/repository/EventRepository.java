package com.ingressos.api.repository;

import com.ingressos.api.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e" +
            " where (:name is null or e.name like %:name%)" +
            " and e.date between :startDate and :endDate")
    Page<Event> search(@Param("name") String name,
                       @Param("startDate") LocalDateTime startDate,
                       @Param("endDate") LocalDateTime endDate,
                       Pageable pageable);

}
