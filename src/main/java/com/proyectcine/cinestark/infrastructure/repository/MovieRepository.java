package com.proyectcine.cinestark.infrastructure.repository;

import com.proyectcine.cinestark.domain.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Movie save(Movie movie);
    Optional<Movie> findById(Long id);
    Page<Movie> findAll(Pageable pageable);

}
