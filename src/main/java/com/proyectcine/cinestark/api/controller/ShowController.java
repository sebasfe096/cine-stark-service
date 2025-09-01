package com.proyectcine.cinestark.api.controller;

import com.proyectcine.cinestark.api.dto.response.GenericResponse;
import com.proyectcine.cinestark.domain.service.ShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/api/shows")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ShowController {

    private final ShowService showService;

    @GetMapping("/{showId}/seats")
    public ResponseEntity<GenericResponse> getSeats(@PathVariable Long showId, @RequestHeader("Authorization") String token) {
        log.info("Begin service getSeats");
        return ResponseEntity.ok(showService.getSeatsByShowId(showId, token));
    }
}
