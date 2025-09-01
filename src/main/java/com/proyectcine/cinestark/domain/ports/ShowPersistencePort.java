package com.proyectcine.cinestark.domain.ports;

import com.proyectcine.cinestark.domain.model.Show;

import java.util.List;
import java.util.Optional;

public interface ShowPersistencePort {

    List<Show> findByMovieId(Long movieId);

    Optional<Show> findById(Long id);

    Show save(Show show);

    Optional<Show> findByShowMovieId(Long movieId);


}
