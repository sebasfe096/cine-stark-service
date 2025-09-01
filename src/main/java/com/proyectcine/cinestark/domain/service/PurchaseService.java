package com.proyectcine.cinestark.domain.service;

import com.proyectcine.cinestark.api.dto.request.PurchaseRequest;
import com.proyectcine.cinestark.api.dto.response.GenericResponse;

public interface PurchaseService {

    GenericResponse createPurchase(PurchaseRequest request, String token) throws Exception;

    GenericResponse getPurchases(int page, int size, String document, String token);
}
