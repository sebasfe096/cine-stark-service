package com.proyectcine.cinestark.infrastructure.adapter;

import com.proyectcine.cinestark.domain.model.User;
import com.proyectcine.cinestark.domain.ports.UserPersistencePort;
import com.proyectcine.cinestark.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserJpaAdapter implements UserPersistencePort {

    private final UserRepository userRepository;

    public UserJpaAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }
}
