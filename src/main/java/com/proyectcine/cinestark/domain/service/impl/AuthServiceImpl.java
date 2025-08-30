package com.proyectcine.cinestark.domain.service.impl;

import com.proyectcine.cinestark.api.dto.request.LoginRequest;
import com.proyectcine.cinestark.api.dto.response.LoginResponse;
import com.proyectcine.cinestark.domain.excepcion.BusinessException;
import com.proyectcine.cinestark.domain.model.User;
import com.proyectcine.cinestark.domain.ports.UserPersistencePort;
import com.proyectcine.cinestark.domain.service.AuthService;
import com.proyectcine.cinestark.domain.auth.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthServiceImpl implements AuthService {

    private final UserPersistencePort userPersistencePort;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Begin login");
        User user = userPersistencePort.findByEmail(request.getEmail());

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtService.generateToken(user);

        return LoginResponse.builder().email(request.getEmail()).role(user.getRole().getName())
                .type("Bearer").expiration(99999).token(token).build();
    }
}
