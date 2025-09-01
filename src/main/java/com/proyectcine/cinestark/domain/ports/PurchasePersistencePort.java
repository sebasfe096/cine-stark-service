package com.proyectcine.cinestark.domain.ports;

import com.proyectcine.cinestark.domain.model.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PurchasePersistencePort {

    Purchase save(Purchase purchase);

    Page<Object[]> findByCustomerDocument(String document, Pageable pageable);

    Page<Purchase> findAll(Pageable pageable);
}
