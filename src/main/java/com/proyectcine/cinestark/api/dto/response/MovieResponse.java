package com.proyectcine.cinestark.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponse {

    private Long id;
    private String title;
    private String description;
    private String genre;
    private Integer duration;
    private String releaseDate;
    private String imageUrl;
    private Boolean enabled;
}
