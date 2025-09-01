package com.proyectcine.cinestark.domain.ports;

import com.proyectcine.cinestark.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserPersistencePort {

    User save(User user);

    User findByEmail(String email);

    Optional<User> findById(Long id);

    Page<User> findAll(Pageable pageable);
}
