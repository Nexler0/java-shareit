package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

@Data
@Builder
public class ItemDto {
    private Long id;
    private Long userId;
    private Long requestId;
    private String name;
    private String description;
    private Boolean available;
}
