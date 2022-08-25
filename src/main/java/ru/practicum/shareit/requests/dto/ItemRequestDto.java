package ru.practicum.shareit.requests.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.ItemShort;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDto {

    private Long id;
    private String description;
    private Long requesterId;
    private LocalDateTime created;
    private List<ItemShort> items;
}
