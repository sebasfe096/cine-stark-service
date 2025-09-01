package com.proyectcine.cinestark.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data

@NoArgsConstructor
@Builder
public class MovieWithShowsResponse {

    private Long id;
    private String title;
    private String description;
    private String genre;
    private Integer duration;
    private String imageUrl;
    private Boolean enabled;
    private List<ShowDTO> shows;

    public MovieWithShowsResponse(Long id, String title, String description, String genre,
                          Integer duration, String imageUrl, Boolean enabled, List<ShowDTO> shows) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.duration = duration;
        this.imageUrl = imageUrl;
        this.enabled = enabled;
        this.shows = shows;
    }
}
