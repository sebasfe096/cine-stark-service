package com.proyectcine.cinestark.infrastructure.adapter;

import com.proyectcine.cinestark.domain.model.Seat;
import com.proyectcine.cinestark.domain.ports.SeatPersistencePort;
import com.proyectcine.cinestark.infrastructure.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SeatJpaAdapter implements SeatPersistencePort {

    private final SeatRepository seatRepository;

    @Override
    public void save(List<Seat> seat) {
        seatRepository.saveAll(seat);
    }

    @Override
    public List<Seat> findByShowId(Long showId) {
        return seatRepository.findByShowId(showId);
    }
}
