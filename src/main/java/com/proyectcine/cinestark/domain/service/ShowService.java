package com.proyectcine.cinestark.domain.service;

import com.proyectcine.cinestark.api.dto.response.GenericResponse;

public interface ShowService {

    GenericResponse getSeatsByShowId(Long showId, String token);
}
