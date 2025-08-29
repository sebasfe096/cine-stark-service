package com.proyectcine.cinestark.domain.service.impl;

import com.proyectcine.cinestark.api.dto.request.RegisterRequest;
import com.proyectcine.cinestark.api.dto.response.GenericResponse;
import com.proyectcine.cinestark.domain.excepcion.BusinessException;
import com.proyectcine.cinestark.domain.model.Role;
import com.proyectcine.cinestark.domain.model.User;
import com.proyectcine.cinestark.domain.ports.UserPersistencePort;
import com.proyectcine.cinestark.domain.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

    private final UserPersistencePort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public GenericResponse registerUser(RegisterRequest request) {
        log.info("Begin method registerUser");

        if (userRepositoryPort.findByEmail(request.getEmail()) != null) {
            throw new BusinessException("El correo ya está registrado", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.setCreatedBy(1L);
        user.setCreatedAt(LocalDateTime.now());

        Role role = new Role();
        role.setId(1L);
        user.setRole(role);

        User userSave = userRepositoryPort.save(user);

        return GenericResponse.buildGenericPortalResponseDTO("canContinue", true, null, null, 200L, "Consulta exitosa.");
    }
}
