package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;

@Data
@RequiredArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    @Email
    private String email;
}
