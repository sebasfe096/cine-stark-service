package com.proyectcine.cinestark.domain.service.impl;

import com.proyectcine.cinestark.api.dto.response.GenericResponse;
import com.proyectcine.cinestark.api.dto.response.SeatDTO;
import com.proyectcine.cinestark.api.dto.response.ShowSeatsResponse;
import com.proyectcine.cinestark.domain.auth.JwtService;
import com.proyectcine.cinestark.domain.excepcion.BusinessException;
import com.proyectcine.cinestark.domain.model.Show;
import com.proyectcine.cinestark.domain.ports.SeatPersistencePort;
import com.proyectcine.cinestark.domain.ports.ShowPersistencePort;
import com.proyectcine.cinestark.domain.service.ShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ShowServiceImpl implements ShowService {

    private final ShowPersistencePort showPersistencePort;

    private final SeatPersistencePort seatPersistencePort;

    private final JwtService jwtService;

    @Override
    public GenericResponse getSeatsByShowId(Long showId, String token) {
        log.info("Begin method getSeatsByShowId");
        jwtService.validateToken(token);
        Show show = showPersistencePort.findById(showId)
                .orElseThrow(() -> new BusinessException("Show no encontrado", HttpStatus.NOT_FOUND));

        List<SeatDTO> seatDTOs = seatPersistencePort.findByShowId(showId)
                .stream()
                .map(seat -> new SeatDTO(seat.getSeatNumber(), !seat.getAvailable()))
                .toList();

        var response = new ShowSeatsResponse(
                show.getId(),
                show.getMovie().getId(),
                show.getShowDate(),
                show.getShowTime(),
                seatDTOs
        );
        return GenericResponse.buildGenericPortalResponseDTO(null, response, null, null, 200L, "Consulta exitosa.");
    }
}
