package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.EmptyListException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplTest {

    private final EntityManager em;
    private final UserService userService;

    @Test
    void addValidUserTest() {
        User user = new User();
        user.setEmail("user@123.ru");
        user.setName("Jef");

        User user1 = userService.addUser(user);

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

        userService.getAllUsers().forEach(System.out::println);
        assertThat(userService.getUserById(5L), equalTo(user2));
    }
}