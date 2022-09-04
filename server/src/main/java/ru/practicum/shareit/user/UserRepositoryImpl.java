package ru.practicum.shareit.user;

import org.springframework.context.annotation.Lazy;

public class UserRepositoryImpl implements UserRepositoryCustom {

    private final UserRepository userRepository;

    public UserRepositoryImpl(@Lazy UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
