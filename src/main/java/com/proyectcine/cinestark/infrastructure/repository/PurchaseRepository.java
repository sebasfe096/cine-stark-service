package com.proyectcine.cinestark.infrastructure.repository;

import com.proyectcine.cinestark.domain.model.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query(value = """
        SELECT p.email,
               p.full_name,
               p.document_number,
               p.bank,
               p.total_amount,
               s.show_date AS date,
               s.show_time AS showTime,
               STRING_AGG(ps.seat_number, ',') AS seats
        FROM purchase p
        JOIN purchase_seat ps ON ps.purchase_id = p.id
        JOIN show s ON s.id = p.show_id
        WHERE (:document IS NULL OR :document = '' OR p.document_number ILIKE %:document%)
        GROUP BY p.id, s.show_date, s.show_time
        """,
            countQuery = """
        SELECT COUNT(DISTINCT p.id)
        FROM purchase p
        WHERE (:document IS NULL OR :document = '' OR p.document_number ILIKE %:document%)
        """,
            nativeQuery = true)
    Page<Object[]> findPurchasesByDocument(@Param("document") String document, Pageable pageable);

    Page<Purchase> findAll(Pageable pageable);
}
