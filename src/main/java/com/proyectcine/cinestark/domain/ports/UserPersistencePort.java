package com.proyectcine.cinestark.domain.ports;

import com.proyectcine.cinestark.domain.model.User;

public interface UserPersistencePort {

    User save(User user);

    User findByEmail(String email);
}
