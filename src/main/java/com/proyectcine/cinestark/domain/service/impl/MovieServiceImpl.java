package com.proyectcine.cinestark.domain.service.impl;

import com.proyectcine.cinestark.api.dto.request.MovieRequest;
import com.proyectcine.cinestark.api.dto.response.*;
import com.proyectcine.cinestark.domain.excepcion.BusinessException;
import com.proyectcine.cinestark.domain.model.Movie;
import com.proyectcine.cinestark.domain.model.Show;
import com.proyectcine.cinestark.domain.ports.MoviePersistencePort;
import com.proyectcine.cinestark.domain.ports.ShowPersistencePort;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MovieServiceImpl implements MovieService {


    private final MoviePersistencePort moviePersistencePort;
    private final ShowPersistencePort showPersistencePort;

    private final JwtService jwtService;
    private final CloudinaryService cloudinaryService;


    @Override
    public GenericResponse createMovie(MovieRequest request, String token) {
        log.info("Begin method createMovie");

        jwtService.validateToken(token);
        String userId = jwtService.extractUserId(token).toString();

        Movie movie = buildMovie(request, userId);

        Movie savedMovie = moviePersistencePort.save(movie);

        Show show = buildShow(request, savedMovie, userId);

        showPersistencePort.save(show);

        return GenericResponse.buildGenericPortalResponseDTO(
                "canContinue",
                true,
                null,
                null,
                200L,
                "Película y función creadas exitosamente."
        );
    }

    private Movie buildMovie(MovieRequest request, String userId) {
        LocalDateTime now = LocalDateTime.now();
        return Movie.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .genre(request.getGenre())
                .duration(request.getDuration())
                .releaseDate(LocalDateTime.from(request.getReleaseDate()))
                .createdBy(userId)
                .createdAt(now)
                .updatedBy(userId)
                .updatedAt(now)
                .enabled(true)
                .imageUrl(getUrlAndUploadImage(request.getImage()))
                .build();
    }

    private Show buildShow(MovieRequest request, Movie movie, String userId) {
        LocalDateTime now = LocalDateTime.now();
        return Show.builder()
                .movie(movie)
                .showDate(request.getShowDate())
                .showTime(request.getShowTime())
                .createdBy(userId)
                .createdAt(now)
                .updatedBy(userId)
                .updatedAt(now)
                .build();
    }

    @Override
    public GenericResponse getAllMovies(String token, int page, int size) {
        log.info("Begin method getAllMovies | page: {}, size: {}", page, size);

        // ✅ Validar token antes de continuar
        jwtService.validateToken(token);

        Pageable pageable = PaginationBuilder.createSort("id", "desc", page, size);
        Page<Movie> moviesPage = moviePersistencePort.findAll(pageable);

        List<MovieResponse> movies = moviesPage.getContent()
                .stream()
                .map(this::mapToMovieResponse) // ✅ Delegamos la conversión a un método privado
                .toList();

        MetaResponseDTO meta = MetaResponseDTO.buildMetaResponseDTO(moviesPage, page, size);

        log.info("End method getAllMovies | movies found: {}", movies.size());

        return GenericResponse.buildGenericPortalResponseDTO(
                "movies",
                movies,
                meta,
                null,
                200L,
                "Consulta exitosa."
        );
    }

    private MovieResponse mapToMovieResponse(Movie movie) {
        Optional<Show> showOptional = showPersistencePort.findByShowMovieId(movie.getId());

        String showDate = showOptional
                .map(Show::getShowDate)
                .map(Object::toString)
                .orElse(null);

        String showTime = showOptional
                .map(Show::getShowTime)
                .map(Object::toString)
                .orElse(null);

        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .genre(movie.getGenre())
                .duration(movie.getDuration())
                .releaseDate(movie.getReleaseDate() != null
                        ? movie.getReleaseDate().toLocalDate().toString()
                        : null)
                .imageUrl(movie.getImageUrl())
                .enabled(movie.getEnabled())
                .showDate(showDate)
                .showTime(showTime)
                .build();
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
        movie.setReleaseDate(LocalDateTime.from(request.getReleaseDate()));
        movie.setUpdatedAt(LocalDateTime.now());
        movie.setUpdatedBy(jwtService.extractUserId(token).toString());
        movie.setEnabled(request.getEnabled());
        movie.setImageUrl(getUrlAndUploadImage(request.getImage()));

        moviePersistencePort.save(movie);

        return GenericResponse.buildGenericPortalResponseDTO("canContinue", true, null, null, 200L, "Consulta exitosa.");
    }

    @Override
    public GenericResponse getActiveMoviesWithShows() {
        log.info("Begin method getActiveMoviesWithShows");

        List<Movie> movies = moviePersistencePort.findByEnabledTrue();

        List<MovieWithShowsResponse> movieResponses = movies.stream()
                .map(this::mapMovieToMovieWithShowsResponse)
                .toList();

        return GenericResponse.buildGenericPortalResponseDTO(
                null,
                movieResponses,
                null,
                null,
                200L,
                "Consulta exitosa."
        );
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

    private MovieWithShowsResponse mapMovieToMovieWithShowsResponse(Movie movie) {
        List<Show> shows = showPersistencePort.findByMovieId(movie.getId());

        List<ShowDTO> showDTOs = shows.stream()
                .map(show -> ShowDTO.builder()
                        .showId(show.getId())
                        .date(show.getShowDate())
                        .times(List.of(show.getShowTime().toString()))
                        .build())
                .toList();

        return MovieWithShowsResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .genre(movie.getGenre())
                .duration(movie.getDuration())
                .imageUrl(movie.getImageUrl())
                .enabled(movie.getEnabled())
                .shows(showDTOs)
                .build();
    }

}
