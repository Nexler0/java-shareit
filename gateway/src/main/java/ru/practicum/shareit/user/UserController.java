package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getUsersList() {
        log.info("Call method: UserController.getUserList");
        return userClient.getUsersList();
    }

    @PostMapping
    public ResponseEntity<Object> postUser(@Valid @RequestBody UserDto userDto) {
        log.info("Call method: UserController.postUser");
        return userClient.postUser(userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        log.info("Call method: UserController.getUserById: {}", id);
        return userClient.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable Long id) {
        log.info("Call method: UserController.deleteUserById: {}", id);
        return userClient.deleteUser(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        log.info("Call method: UserController.updateUser: {}, Body: {}", id, userDto.toString());
        return userClient.updateUser(id, userDto);
    }
}
