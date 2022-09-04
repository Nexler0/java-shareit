package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getUsersList() {
        return get("");
    }

    public ResponseEntity<Object> postUser(UserDto userDto) {
        isValidateCreateUser(userDto);
        return post("", userDto);
    }

    public ResponseEntity<Object> getUserById(Long id) {
        return get("/" + id);
    }

    public ResponseEntity<Object> deleteUser(Long id) {
        return delete("/" + id);
    }

    public ResponseEntity<Object> updateUser(Long id, UserDto userDto) {
        return patch("/" + id, userDto);
    }

    private boolean isValidateCreateUser(UserDto userDto) {
        if (userDto.getEmail() != null && !userDto.getName().isEmpty() &&
                userDto.getEmail() != null && !userDto.getEmail().isEmpty()) {
            return true;
        }
        if (userDto.getEmail() == null) {
            throw new ValidationException("Ошибка валидации");
        }
        throw new ConflictException("Ошибка валидации");
    }
}
