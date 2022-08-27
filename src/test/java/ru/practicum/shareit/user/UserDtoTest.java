package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserMapper;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@JsonTest
public class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> json;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Harri", "Harri@mail.ru");
    }

    @Test
    void serializeTest() throws IOException {
        System.out.println(user.equals(new User(1L, "Harri", "Harri@mail.ru")));
        System.out.println(user.hashCode());
        JsonContent<UserDto> userJson = this.json.write(UserMapper.toDto(user));
        assertThat(userJson.getJson(),
                is("{\"id\":1,\"name\":\"Harri\",\"email\":\"Harri@mail.ru\"}"));
    }
}
