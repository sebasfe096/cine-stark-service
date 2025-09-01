package com.proyectcine.cinestark.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowSeatsResponse {

    private Long showId;
    private Long movieId;
    private LocalDate date;
    private LocalTime time;
    private List<SeatDTO> seats;
}
