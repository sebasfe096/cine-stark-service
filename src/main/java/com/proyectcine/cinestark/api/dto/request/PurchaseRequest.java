package com.proyectcine.cinestark.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseRequest {
    private String email;
    private String fullName;
    private String bank;
    private Long showId;
    private String documentNumber;
    private List<String> seats;
    private BigDecimal totalAmount;
    private String time;
}
