package com.proyectcine.cinestark.domain.service;

import com.proyectcine.cinestark.api.dto.request.LoginRequest;
import com.proyectcine.cinestark.api.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}
