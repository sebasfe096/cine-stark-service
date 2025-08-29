package com.proyectcine.cinestark.domain.service;

import com.proyectcine.cinestark.api.dto.request.RegisterRequest;
import com.proyectcine.cinestark.api.dto.response.GenericResponse;

public interface UserService {

    GenericResponse registerUser(RegisterRequest request);
}
