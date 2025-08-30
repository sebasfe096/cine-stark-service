package com.proyectcine.cinestark.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "movie")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "genre", nullable = false)
    private String genre;

    @Column(name = "imageUrl", nullable = false)
    private String imageUrl;

    @Column(name = "releaseDate", nullable = false)
    private LocalDateTime releaseDate;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdBy", nullable = false)
    private String createdBy;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "updatedBy", nullable = false)
    private String updatedBy;
}
