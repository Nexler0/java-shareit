package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> listUsers() {
        return userService.getAllUsers().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody User user) {
        return UserMapper.toDto(userService.addUser(user));
    }

    @GetMapping("/{id}")
    public UserDto userById(@PathVariable Long id) {
        return UserMapper.toDto(userService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public UserDto updateUser(@PathVariable Long id, @Valid @RequestBody UserDto user) {
        return UserMapper.toDto(userService.updateUser(id, UserMapper.toUser(user)));
    }

}
