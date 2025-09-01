package com.proyectcine.cinestark.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponseDTO {

    private String fullName;
    private String email;
    private String documentNumber;
    private String bank;
    private Double cost;
    private String seats;
    private String date;
    private String showTime;
}
