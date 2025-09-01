package com.proyectcine.cinestark.infrastructure.repository;

import com.proyectcine.cinestark.domain.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {

    @Query("SELECT s FROM Show s WHERE s.movie.id = :movieId")
    List<Show> findByMovieId(@Param("movieId") Long movieId);

    Optional<Show> findShowByMovieId(Long movieId);

}
