package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.EmptyListException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(User user) {
        if (user != null && isValidateCreateUser(user)) {
            return userRepository.save(user);
        }
        throw new EmptyListException("Сервера");
    }

    private boolean isValidateCreateUser(User user) {
        if (user.getEmail() != null && !user.getName().isEmpty()
                && user.getEmail() != null && !user.getEmail().isEmpty()
                && !userRepository.findAll().contains(user)) {
            return true;
        }
        if (user.getEmail() == null) {
            throw new ValidationException("Ошибка валидации");
        }
        throw new ConflictException("Ошибка валидации");
    }

    private boolean isValidateUpdateUser(User user) {
        if (user.getId() == null || user.getId() == 0) {
//            return !user.equals(userRepository.getReferenceById(user.getId()));
            return false;
        } else if (user.getId() > 0) {
            return userRepository.findAll().stream().noneMatch(user1 -> user1.equals(user));
        } else {
            if (userRepository.findAll().stream().anyMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
                throw new EmptyListException("Ошибка валидации email");
            } else {
                return true;
            }
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();

        if (!users.isEmpty()) {
            log.info("Запрос пользователей, в списке {}", users.size());
            return users;
        } else {
            throw new EmptyListException("Список пуст");
        }
    }

    @Override
    public User getUserById(Long userId) {
        User user = userRepository.getReferenceById(userId);
        if (user.getName() != null) {
            return user;
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public User updateUser(Long userId, User user) {
        User oldUser = getUserById(userId);

        if (user.getEmail() != null && !user.getEmail().isEmpty() && isValidateUpdateUser(user)) {
            oldUser.setEmail(user.getEmail());
            userRepository.deleteById(userId);
        }
        if (user.getName() != null && !user.getName().isEmpty() && isValidateUpdateUser(user)) {
            oldUser.setName(user.getName());
            userRepository.deleteById(userId);
        }
        userRepository.save(oldUser);
        return oldUser;
    }
}
