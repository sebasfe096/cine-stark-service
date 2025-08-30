package com.proyectcine.cinestark.infrastructure.adapter;

import com.proyectcine.cinestark.domain.excepcion.BusinessException;
import com.proyectcine.cinestark.domain.model.Movie;
import com.proyectcine.cinestark.domain.ports.MoviePersistencePort;
import com.proyectcine.cinestark.infrastructure.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MovieJpaAdapter implements MoviePersistencePort {

    private final MovieRepository movieRepository;

    private final List<Movie> movies = new ArrayList<>();

    @Override
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    @Override
    public Page<Movie> findAll(Pageable page) {
        return movieRepository.findAll(page);
    }

    @Override
    public Movie update(Movie movie) {
        return movieRepository.save(movie);
    }
}
