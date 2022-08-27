package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.EmptyListException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceImplTest {

    private final EntityManager em;
    private final UserService userService;


    @Test
    void addValidUserTest() {
        User user = new User();
        user.setEmail("user@123.ru");
        user.setName("Jef");
        User user1 = userService.addUser(user);
        UserMapper.toDto(user);
        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        User checkUser = query.setParameter("id", user1.getId()).getSingleResult();
        assertThat(checkUser.getId(), equalTo(user.getId()));
    }

    @Test
    void addInvalidUserNameTest() {
        User user = new User();
        user.setEmail("");
        user.setName("");
        Throwable throwable = assertThrows(ConflictException.class, () -> userService.addUser(user));
        assertThat(throwable.getMessage(), is("Ошибка валидации"));
    }

    @Test
    void addInvalidUserEmailTest() {
        User user = new User();
        user.setEmail(null);
        user.setName("Jef");
        Throwable throwable = assertThrows(ValidationException.class, () -> userService.addUser(user));
        assertThat(throwable.getMessage(), is("Ошибка валидации"));
    }

    @Test
    void addEmptyUserTest() {
        User user = null;
        Throwable throwable = assertThrows(EmptyListException.class, () -> userService.addUser(user));
        assertThat(throwable.getMessage(), is("Ошибка сервера"));
    }

    @Test
    void getAllUsersTest() {
        User user = new User(null, "Jef", "Jef@mail.ru");
        User user2 = new User(null, "Sara", "Sara@mail.ru");
        userService.addUser(user);
        userService.addUser(user2);
        assertThat(userService.getAllUsers(), equalTo(List.of(user, user2)));
    }

    @Test
    void getAllUsersFromEmptyListTest() {
        assertThrows(EmptyListException.class, userService::getAllUsers);
    }

    @Test
    void getUserByIdTest() {
        User user = new User(null, "Jef", "Jef@mail.ru");
        User user2 = new User(null, "Sara", "Sara@mail.ru");
        userService.addUser(user);
        userService.addUser(user2);
        assertThat(userService.getUserById(2L), equalTo(user2));
        assertThrows(NotFoundException.class, () -> userService.getUserById(55L));
    }

    @Test
    void updateUserTest() {
        User user = new User(null, "Jef", "Jef@mail.ru");
        userService.addUser(user);
        user = new User(1L, "UpdateJef", "UpdateJef@mail.ru");
        userService.updateUser(1L, user);
        assertThat(userService.getUserById(1L), equalTo(user));
    }

    @Test
    void updateUserWrongEmailTest() {
        User user = new User(null, "Jef", "Jef@mail.ru");
        userService.addUser(user);
        User user1 = new User(null, "UpdateJef", "@mail.ru");
        Throwable throwable = assertThrows(EmptyListException.class,
                () -> userService.updateUser(1L, user1));
        assertThat(throwable.getMessage(), is("Ошибка валидации email"));

    }

    @Test
    void addUserForSameEmailTest() {
        User user = new User(null, "Jef", "Jef12@mail.ru");
        userService.addUser(user);
        User newUser = new User(3L, "UpdateJef", "Jef12@mail.ru");
        assertThrows(DataIntegrityViolationException.class, () -> userService.addUser(newUser));
    }

    @Test
    void deleteUserTest() {
        User user = new User(null, "Jef", "Jef12@mail.ru");
        userService.addUser(user);
        assertThat(userService.getUserById(1L), equalTo(user));
        userService.deleteUser(1L);
        assertThrows(NotFoundException.class, () -> userService.getUserById(1L));
    }
}