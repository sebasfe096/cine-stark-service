package com.proyectcine.cinestark.domain.service.impl;

import com.proyectcine.cinestark.api.dto.request.PurchaseRequest;
import com.proyectcine.cinestark.api.dto.response.GenericResponse;
import com.proyectcine.cinestark.api.dto.response.MetaResponseDTO;
import com.proyectcine.cinestark.api.dto.response.PurchaseResponseDTO;
import com.proyectcine.cinestark.domain.auth.JwtService;
import com.proyectcine.cinestark.domain.excepcion.BusinessException;
import com.proyectcine.cinestark.domain.model.Purchase;
import com.proyectcine.cinestark.domain.model.PurchaseSeat;
import com.proyectcine.cinestark.domain.model.Seat;
import com.proyectcine.cinestark.domain.model.Show;
import com.proyectcine.cinestark.domain.ports.PurchasePersistencePort;
import com.proyectcine.cinestark.domain.ports.SeatPersistencePort;
import com.proyectcine.cinestark.domain.ports.ShowPersistencePort;
import com.proyectcine.cinestark.domain.service.PurchaseService;
import com.proyectcine.cinestark.domain.utils.EmailService;
import com.proyectcine.cinestark.domain.utils.PaginationBuilder;
import com.proyectcine.cinestark.domain.utils.QrGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchasePersistencePort purchasePersistencePort;
    private final SeatPersistencePort seatPersistencePort;
    private final ShowPersistencePort showPersistencePort;

    private final JwtService jwtService;
    private final EmailService emailService;

    @Override
    @Transactional
    public GenericResponse createPurchase(PurchaseRequest request, String token) throws Exception {
        log.info("Begin createPurchase");

        String userId = getUserIdFromToken(token);

        Purchase purchase = create(request, userId);
        var result = purchasePersistencePort.save(purchase);

        Show show = showPersistencePort.findById(request.getShowId())
                .orElseThrow(() -> new BusinessException("Función no encontrada", HttpStatus.NOT_FOUND));

        List<Seat> seats = createReservedSeats(request.getSeats(), show, request.getEmail());
        seatPersistencePort.save(seats);
        sendEmail(result, show, request);


        return GenericResponse.buildGenericPortalResponseDTO(
                "canContinue",
                true,
                null,
                null,
                200L,
                "Consulta exitosa."
        );
    }


    @Override
    public GenericResponse getPurchases(int page, int size, String document, String token) {
        log.info("Begin getPurchases");
        getUserIdFromToken(token);

        Pageable pageable = PaginationBuilder.createSort("id", "desc", page, size);

        Page<Object[]> purchasesPage = purchasePersistencePort.findByCustomerDocument(document, pageable);

        List<PurchaseResponseDTO> purchases = purchasesPage.getContent()
                .stream()
                .map(p -> {
                    try {
                        String showTime = p[6] != null ? p[6].toString() : null;


                        return PurchaseResponseDTO.builder()
                                .email((String) p[0])              // c.email
                                .fullName((String) p[1])           // c.name
                                .documentNumber((String) p[2])     // c.document
                                .bank((String) p[3])               // p.bank
                                .cost(((Number) p[4]).doubleValue()) // p.cost
                                .date(p[5].toString())
                                .showTime(showTime)
                                .seats(p[7].toString())
                                .build();
                    } catch (Exception e) {
                        log.error("Error mapping purchase row", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        MetaResponseDTO meta = MetaResponseDTO.buildMetaResponseDTO(purchasesPage, page, size);

        return GenericResponse.buildGenericPortalResponseDTO(
                "purchases",
                purchases,
                meta,
                null,
                200L,
                "Consulta exitosa."
        );
    }

    private String getUserIdFromToken(String token) {
        jwtService.validateToken(token);
        return jwtService.extractUserId(token).toString();
    }



    public Purchase create(PurchaseRequest request, String userId) {
        Purchase purchase = new Purchase();
        purchase.setEmail(request.getEmail());
        purchase.setFullName(request.getFullName());
        purchase.setBank(request.getBank());
        purchase.setShowId(request.getShowId());
        purchase.setTotalAmount(request.getTotalAmount());
        purchase.setCreatedBy(userId);
        purchase.setCreatedAt(LocalDateTime.now());
        purchase.setUpdatedAt(null);
        purchase.setUpdatedBy(null);
        purchase.setUserId(Long.parseLong(userId));
        purchase.setTime(request.getTime());
        purchase.setDocumentNumber(request.getDocumentNumber());

        List<PurchaseSeat> seats = request.getSeats().stream()
                .map(seatNumber -> {
                    PurchaseSeat ps = new PurchaseSeat();
                    ps.setSeatNumber(seatNumber);
                    ps.setCreatedBy(userId);
                    ps.setCreatedAt(LocalDateTime.now());
                    ps.setPurchase(purchase);
                    ps.setUpdatedAt(null);
                    ps.setUpdatedBy(null);
                    return ps;
                })
                .toList();

        purchase.setSeats(seats);
        return purchase;
    }

    public List<Seat> createReservedSeats(List<String> seatNumbers, Show show, String createdBy) {
        return seatNumbers.stream()
                .map(seatNumber -> {
                    Seat seat = new Seat();
                    seat.setSeatNumber(seatNumber);
                    seat.setAvailable(false);
                    seat.setShow(show);
                    seat.setCreatedAt(LocalDateTime.now());
                    seat.setCreatedBy(createdBy);
                    seat.setUpdatedAt(LocalDateTime.now());
                    seat.setUpdatedBy(createdBy);
                    return seat;
                })
                .toList();
    }

    void sendEmail(Purchase purchase, Show show, PurchaseRequest request) throws Exception {
        log.info("begin method sendEmail");
        byte[] qrCode = QrGenerator.generateQRCode(
                "COMPRA-" + purchase.getId(), 300, 300
        );

        emailService.sendPurchaseEmail(
                purchase.getEmail(),
                "🎬 Confirmación de compra - Cine Stark",
                "<h1>¡Gracias por tu compra!</h1>"
                        + "<p>Función: " + show.getMovie().getTitle() + "</p>"
                        + "<p>Fecha: " + show.getShowDate()+request.getTime() + "</p>"
                        + "<p>Horario: " + show.getShowDate() + "</p>"
                        + "<p>Asientos: " + String.join(", ", request.getSeats()) + "</p>"
                        + "<p>Presenta este QR en taquilla:</p>",
                qrCode
        );
    }
}
