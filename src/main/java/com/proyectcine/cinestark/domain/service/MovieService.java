package com.proyectcine.cinestark.domain.service;

import com.proyectcine.cinestark.api.dto.request.MovieRequest;
import com.proyectcine.cinestark.api.dto.response.GenericResponse;

public interface MovieService {

    GenericResponse createMovie(MovieRequest request, String token);
    GenericResponse getAllMovies(String token, int page, int size);
    GenericResponse updateMovie(Long id, MovieRequest request, String token);
    void disableMovie(Long id, String token);
}
