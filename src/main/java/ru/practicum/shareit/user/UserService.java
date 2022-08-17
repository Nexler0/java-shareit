package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User addUser(User user);

    List<User> getAllUsers();

    User getUserById(Long userId);

    void deleteUser(Long id);

    User updateUser(Long id, User user);

}
