package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.EmptyListException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User addUser(User user) {
        if (user != null && isValidateCreateUser(user)) {
            return userRepository.save(user);
        }
        throw new EmptyListException("Ошибка сервера");
    }

    private boolean isValidateCreateUser(User user) {
        if (user.getEmail() != null && !user.getName().isEmpty() &&
                user.getEmail() != null && !user.getEmail().isEmpty()) {
            return true;
        }
        if (user.getEmail() == null) {
            throw new ValidationException("Ошибка валидации");
        }
        throw new ConflictException("Ошибка валидации");
    }

    private boolean isValidateUpdateUser(User user) {
        if (user != null) {
            if (user.getId() != null) {
                return userRepository.findAll().stream().noneMatch(user1 -> user1.equals(user));
            } else {
                if (userRepository.findByEmailContainingIgnoreCase(user.getEmail()) != null) {
                    throw new EmptyListException("Ошибка валидации email");
                } else {
                    return true;
                }
            }
        } else {
            return false;
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
        if (userRepository.existsUserById(userId)) {
            return userRepository.getReferenceById(userId);
        }
        throw new NotFoundException("Пользователь не найден");
    }

    @Transactional
    @Override
    public User updateUser(Long userId, User user) {
        User oldUser = getUserById(userId);

        if (user.getEmail() != null && !user.getEmail().isEmpty() && isValidateUpdateUser(user)) {
            oldUser.setEmail(user.getEmail());
        } else {
            user.setEmail(oldUser.getEmail());
        }
        if (user.getName() != null && !user.getName().isEmpty()) {
            oldUser.setName(user.getName());
        } else {
            user.setName(oldUser.getName());
        }
        userRepository.setUserInfoById(user.getName(), user.getEmail(), userId);
        return oldUser;
    }
}
