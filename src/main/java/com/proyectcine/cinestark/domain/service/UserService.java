package com.proyectcine.cinestark.domain.service;

import com.proyectcine.cinestark.api.dto.request.RegisterRequest;
import com.proyectcine.cinestark.api.dto.request.UpdateUserRequest;
import com.proyectcine.cinestark.api.dto.response.GenericResponse;

public interface UserService {

    GenericResponse registerUser(RegisterRequest request);

    GenericResponse getUsers(String token, int page, int size);

    GenericResponse updateUser(String token, UpdateUserRequest request, Long id);
}
