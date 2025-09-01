package com.proyectcine.cinestark.domain.ports;

import com.proyectcine.cinestark.domain.model.Seat;

import java.util.List;

public interface SeatPersistencePort {

    void save(List<Seat> seat);

    List<Seat> findByShowId(Long showId);
}
