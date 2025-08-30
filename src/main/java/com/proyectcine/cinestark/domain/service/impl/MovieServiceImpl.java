package com.proyectcine.cinestark.domain.service.impl;

import com.proyectcine.cinestark.api.dto.request.MovieRequest;
import com.proyectcine.cinestark.api.dto.response.GenericResponse;
import com.proyectcine.cinestark.api.dto.response.MetaResponseDTO;
import com.proyectcine.cinestark.api.dto.response.MovieResponse;
import com.proyectcine.cinestark.domain.excepcion.BusinessException;
import com.proyectcine.cinestark.domain.model.Movie;
import com.proyectcine.cinestark.domain.ports.MoviePersistencePort;
import com.proyectcine.cinestark.domain.service.CloudinaryService;
import com.proyectcine.cinestark.domain.auth.JwtService;
import com.proyectcine.cinestark.domain.service.MovieService;
import com.proyectcine.cinestark.domain.utils.PaginationBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MovieServiceImpl implements MovieService {


    private final MoviePersistencePort moviePersistencePort;

    private final JwtService jwtService;
    private final CloudinaryService cloudinaryService;


    @Override
    public GenericResponse createMovie(MovieRequest request, String token) {
        log.info("Begin method createMovie");
        jwtService.validateToken(token);
        Movie movie = new Movie();
        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setGenre(request.getGenre());
        movie.setDuration(request.getDuration());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setCreatedBy(jwtService.extractUserId(token).toString());
        movie.setCreatedAt(LocalDateTime.now());
        movie.setEnabled(true);
        movie.setUpdatedAt(LocalDateTime.now());
        movie.setUpdatedBy("");
        movie.setImageUrl(getUrlAndUploadImage(request.getImage()));

        moviePersistencePort.save(movie);
        return GenericResponse.buildGenericPortalResponseDTO("canContinue", true, null, null, 200L, "Consulta exitosa.");
    }

    @Override
    public GenericResponse getAllMovies(String token, int page, int size) {
        jwtService.validateToken(token);

        Pageable pageable = PaginationBuilder.createSort("id", "desc", page, size);

        Page<Movie> moviesPage = moviePersistencePort.findAll(pageable);

        List<MovieResponse> movies = moviesPage.getContent()
                .stream()
                .map(movie -> MovieResponse.builder()
                        .id(movie.getId())
                        .title(movie.getTitle())
                        .description(movie.getDescription())
                        .genre(movie.getGenre())
                        .duration(movie.getDuration())
                        .releaseDate(movie.getReleaseDate().toLocalDate().toString())
                        .imageUrl(movie.getImageUrl())
                        .enabled(movie.getEnabled())
                        .build())
                .toList();

        MetaResponseDTO meta = MetaResponseDTO.buildMetaResponseDTO(moviesPage, page, size);

        return GenericResponse.buildGenericPortalResponseDTO("movies", movies, meta, null, 200L, "Consulta exitosa.");
    }

    @Override
    public GenericResponse updateMovie(Long id, MovieRequest request, String token) {
        jwtService.validateToken(token);
        Movie movie = moviePersistencePort.findById(id)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));

        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setGenre(request.getGenre());
        movie.setDuration(request.getDuration());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setUpdatedAt(LocalDateTime.now());
        movie.setUpdatedBy(jwtService.extractUserId(token).toString());
        movie.setEnabled(request.getEnabled());
        movie.setImageUrl(getUrlAndUploadImage(request.getImage()));

        moviePersistencePort.save(movie);

        return GenericResponse.buildGenericPortalResponseDTO("canContinue", true, null, null, 200L, "Consulta exitosa.");
    }

    @Override
    public void disableMovie(Long id, String token) {
        jwtService.validateToken(token);
        Movie movie = moviePersistencePort.findById(id)
                .orElseThrow(() -> new BusinessException("Película no encontrada", HttpStatus.NOT_FOUND));
        movie.setEnabled(false);
        movie.setUpdatedAt(LocalDateTime.now());
        movie.setUpdatedBy(jwtService.extractUserId(token).toString());
        moviePersistencePort.save(movie);
    }


    private String getUrlAndUploadImage(MultipartFile image) {
        String url = "";
        if (image != null && !image.isEmpty()) {
            try {
                url = (String) cloudinaryService.uploadFile(image).get("url");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return url;
    }

    private MovieResponse toResponse() {
        return null;
    }

}
