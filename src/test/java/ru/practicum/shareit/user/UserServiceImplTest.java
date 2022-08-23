package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplTest {

    private final EntityManager em;
    private final UserService userService;
    private final UserController userController;

    @Test
    void addUserTest() {
        User user = new User();
        user.setEmail("user@123.ru");
        user.setName("Jef");

        User user1 = userService.addUser(user);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);

        User checkUser = query.setParameter("id", user1.getId()).getSingleResult();

        assertThat(checkUser.getId(), equalTo(user.getId()));
    }
}