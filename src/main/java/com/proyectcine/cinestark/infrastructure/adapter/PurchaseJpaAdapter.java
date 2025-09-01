package com.proyectcine.cinestark.infrastructure.adapter;

import com.proyectcine.cinestark.domain.model.Purchase;
import com.proyectcine.cinestark.domain.ports.PurchasePersistencePort;
import com.proyectcine.cinestark.infrastructure.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class PurchaseJpaAdapter implements PurchasePersistencePort {

    private final PurchaseRepository purchaseRepository;

    @Override
    public Purchase save(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    @Override
        public Page<Object[]> findByCustomerDocument(String document, Pageable pageable) {
        return purchaseRepository.findPurchasesByDocument(document, pageable);
    }

    @Override
    public Page<Purchase> findAll(Pageable pageable) {
        return purchaseRepository.findAll(pageable);
    }
}
