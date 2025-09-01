package com.proyectcine.cinestark.api.controller;

import com.proyectcine.cinestark.api.dto.request.PurchaseRequest;
import com.proyectcine.cinestark.api.dto.response.GenericResponse;
import com.proyectcine.cinestark.domain.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/api/purchase")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class PurchaseController {

    private final PurchaseService purchaseService;


    @PostMapping
    public ResponseEntity<GenericResponse> createPurchase(@RequestBody PurchaseRequest request, @RequestHeader("Authorization") String token) throws Exception {
        log.info("Begin createPurchase -> {} " , request);
        return ResponseEntity.ok(purchaseService.createPurchase(request, token));
    }

    @GetMapping
    public ResponseEntity<GenericResponse> getPurchases(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        @RequestParam(required = false) String document,
                                                        @RequestHeader("Authorization") String token
    ) {
        return ResponseEntity.ok(purchaseService.getPurchases(page, size, document, token));
    }
}
