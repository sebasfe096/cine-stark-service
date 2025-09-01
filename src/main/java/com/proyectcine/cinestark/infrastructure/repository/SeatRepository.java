package com.proyectcine.cinestark.infrastructure.repository;

import com.proyectcine.cinestark.domain.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByShowId(Long showId);
}
