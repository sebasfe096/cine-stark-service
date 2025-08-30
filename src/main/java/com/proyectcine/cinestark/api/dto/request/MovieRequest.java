package com.proyectcine.cinestark.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequest {
    private String title;
    private String description;
    private String genre;
    private Integer duration;
    private LocalDateTime releaseDate;
    private MultipartFile image;
    private Boolean enabled;
}
