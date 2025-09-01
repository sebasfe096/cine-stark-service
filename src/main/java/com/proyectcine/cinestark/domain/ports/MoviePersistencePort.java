package com.proyectcine.cinestark.domain.ports;

import com.proyectcine.cinestark.domain.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MoviePersistencePort {

    Movie save(Movie movie);
    Optional<Movie> findById(Long id);
    Page<Movie> findAll(Pageable pageable);
    List<Movie> findByEnabledTrue();
    Movie update(Movie movie);
}
