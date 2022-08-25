package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User(1L, "sboot", "sboot@mail.ru"));
    }

    @Test
    void findByIdTest() {
        User user1 = this.userRepository.getUserById(1L);
        assertThat(user1.getName(), equalTo("sboot"));
        assertThat(user1.getEmail(), equalTo("sboot@mail.ru"));
    }

    @Test
    void findByEmailTest() {
        User user1 = this.userRepository.findByEmailContainingIgnoreCase(user.getEmail());
        assertThat(user1.getName(), equalTo("sboot"));
        assertThat(user1.getEmail(), equalTo("sboot@mail.ru"));
    }

    @Test
    void isExistUserByIdTest() {
        assertThat(userRepository.existsUserById(user.getId()), equalTo(true));
        assertThat(userRepository.existsUserById(50L), equalTo(false));
    }

    @Test
    void updateUserTest() {
        assertThat(user.getName(), equalTo("sboot"));
        assertThat(user.getEmail(), equalTo("sboot@mail.ru"));

        User updateUser = new User(user.getId(), "sbootUpdate", "sbootUpdate@mail.ru");
        userRepository.setUserInfoById(updateUser.getName(), updateUser.getEmail(), updateUser.getId());
        userRepository.save(updateUser);
        User checkUser = userRepository.getUserById(updateUser.getId());

        assertThat(checkUser.getName(), equalTo("sbootUpdate"));
        assertThat(checkUser.getEmail(), equalTo("sbootUpdate@mail.ru"));
    }
}
