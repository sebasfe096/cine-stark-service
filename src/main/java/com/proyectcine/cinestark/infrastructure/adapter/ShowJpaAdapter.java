package com.proyectcine.cinestark.infrastructure.adapter;

import com.proyectcine.cinestark.domain.model.Show;
import com.proyectcine.cinestark.domain.ports.ShowPersistencePort;
import com.proyectcine.cinestark.infrastructure.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ShowJpaAdapter implements ShowPersistencePort {

    private final ShowRepository showRepository;

    @Override
    public List<Show> findByMovieId(Long movieId) {
        return showRepository.findByMovieId(movieId);
    }

    @Override
    public Optional<Show> findById(Long id) {
        return showRepository.findById(id);
    }

    @Override
    public Show save(Show show) {
        return showRepository.save(show);
    }

    @Override
    public Optional<Show> findByShowMovieId(Long movieId) {
        return showRepository.findShowByMovieId(movieId);
    }
}
