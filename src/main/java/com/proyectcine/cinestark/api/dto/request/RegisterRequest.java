package com.proyectcine.cinestark.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String documentNumber;
    private String password;
    private LocalDateTime birthDate;
    private Boolean acceptedTerms;

}
