package com.proyectcine.cinestark.domain.service.impl;

import com.proyectcine.cinestark.api.dto.request.RegisterRequest;
import com.proyectcine.cinestark.api.dto.request.UpdateUserRequest;
import com.proyectcine.cinestark.api.dto.response.GenericResponse;
import com.proyectcine.cinestark.api.dto.response.MetaResponseDTO;
import com.proyectcine.cinestark.api.dto.response.UserResponse;
import com.proyectcine.cinestark.domain.auth.JwtService;
import com.proyectcine.cinestark.domain.excepcion.BusinessException;
import com.proyectcine.cinestark.domain.model.Role;
import com.proyectcine.cinestark.domain.model.User;
import com.proyectcine.cinestark.domain.ports.UserPersistencePort;
import com.proyectcine.cinestark.domain.service.UserService;
import com.proyectcine.cinestark.domain.utils.PaginationBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

    private final UserPersistencePort userPersistencePort;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private final int ALLOWED_AGE = 18;

    public GenericResponse registerUser(RegisterRequest request) {
        log.info("Begin method registerUser");

        if (userPersistencePort.findByEmail(request.getEmail()) != null) {
            throw new BusinessException("El correo ya está registrado", HttpStatus.BAD_REQUEST);
        }

        saveUser(request);

        validateAge(request.getBirthDate());

        return GenericResponse.buildGenericPortalResponseDTO("canContinue", true, null, null, 200L, "Consulta exitosa.");
    }

    @Override
    public GenericResponse getUsers(String token, int page, int size) {
        log.info("Begin method getUsers");
        jwtService.validateToken(token);

        Pageable pageable = PaginationBuilder.createSort("id", "desc", page, size);

        Page<User> usersPage = userPersistencePort.findAll(pageable);

        List<UserResponse> users = usersPage.getContent()
                .stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .documentNumber(user.getDocumentNumber())
                        .role(user.getRole().getName())
                        .enabled(user.getEnabled())
                        .birthDate(user.getBirthDate() != null ? user.getBirthDate().toLocalDate().toString() : null)
                        .acceptedTerms(user.getAcceptedTerms())
                        .build())
                .toList();

        MetaResponseDTO meta = MetaResponseDTO.buildMetaResponseDTO(usersPage, page, size);

        return GenericResponse.buildGenericPortalResponseDTO(
                "users",
                users,
                meta,
                null,
                200L,
                "Consulta de usuarios exitosa."
        );
    }

    @Override
    public GenericResponse updateUser(String token, UpdateUserRequest request, Long id) {
        log.info("Begin method updateUser");
        jwtService.validateToken(token);
        User user = userPersistencePort.findById(id)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado", HttpStatus.NOT_FOUND));

        if(!user.getEmail().equalsIgnoreCase(request.getEmail())) {
            validateEmail(request.getEmail());
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setDocumentNumber(request.getDocumentNumber());
        user.setEnabled(request.getEnabled());
        user.setUpdatedAt(LocalDateTime.now());
        user.setUpdatedBy(jwtService.extractUserId(token).toString());

        userPersistencePort.save(user);
        return GenericResponse.buildGenericPortalResponseDTO(
                "canContinue",
                true,
                null,
                null,
                200L,
                "Usuario editado correctamente."
        );
    }

    public void saveUser(RegisterRequest request) {
        log.info("Begin method saveUser");

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.setCreatedBy(1L);
        user.setCreatedAt(LocalDateTime.now());
        user.setBirthDate(request.getBirthDate());
        user.setAcceptedTerms(request.getAcceptedTerms());
        user.setDocumentNumber(request.getDocumentNumber());

        Role role = new Role();
        role.setId(1L);
        user.setRole(role);

        userPersistencePort.save(user);
    }

    public void validateEmail(String email) {
        log.info("Begin method validateEmail");
        if (userPersistencePort.findByEmail(email) != null) {
            throw new BusinessException("El correo ya está registrado", HttpStatus.BAD_REQUEST);
        }
    }

    public void validateAge(LocalDateTime birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula");
        }

        LocalDate birth = birthDate.toLocalDate();
        LocalDate today = LocalDate.now();

        int age = Period.between(birth, today).getYears();

        if (age < ALLOWED_AGE) {
            throw new BusinessException("La persona es menor de edad. Edad actual: " + age, HttpStatus.BAD_REQUEST);
        }
    }
}
