package com.proyectcine.cinestark.api.controller;

import com.proyectcine.cinestark.api.dto.request.MovieRequest;
import com.proyectcine.cinestark.api.dto.response.GenericResponse;
import com.proyectcine.cinestark.domain.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/api/movie")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MovieController {

    private final MovieService movieService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GenericResponse> createMovie(@ModelAttribute MovieRequest request,
                                                     @RequestHeader("Authorization") String token) {
        log.info("Begin service createMovie -> {}", request);
        return ResponseEntity.ok(movieService.createMovie(request, token));
    }

    @GetMapping
    public ResponseEntity<GenericResponse> getAllMovies(@RequestHeader("Authorization") String token,
                                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                                        @RequestParam(value = "size", defaultValue = "5") int size
                                                        ) {
        return ResponseEntity.ok(movieService.getAllMovies(token, page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse> updateMovie(@PathVariable Long id,
                                                       @ModelAttribute MovieRequest request,
                                                       @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(movieService.updateMovie(id, request, token));
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<Void> disableMovie(@PathVariable Long id,@RequestHeader("Authorization") String token) {
        movieService.disableMovie(id, token);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/with-shows")
    public ResponseEntity<GenericResponse> getMoviesWithShows() {
        return ResponseEntity.ok(movieService.getActiveMoviesWithShows());
    }


}
