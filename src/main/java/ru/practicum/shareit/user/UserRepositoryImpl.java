package ru.practicum.shareit.user;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


@Service
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final UserRepository userRepository;

    public UserRepositoryImpl(@Lazy UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    @Override
//    public User addUser(User user) {
//        if (user.getId() == 0) {
//            id++;
//            user.setId(id);
//        }
//        userList.put(user.getId(), user);
//        return user;
//    }
//
//    @Override
//    public void deleteUser(int id) {
//        userList.remove(id);
//    }
//
//    @Override
//    public List<User> getAllUsers() {
//        return new ArrayList<>(userList.values());
//    }
//
//    @Override
//    public User getUserById(int userId) {
//        if (userList.size() > 0) {
//            return userList.get(userId);
//        } else {
//            throw new NotFoundException("Список пользователей пуст");
//        }
//    }

}
