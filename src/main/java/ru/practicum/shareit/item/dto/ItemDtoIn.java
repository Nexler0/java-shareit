package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDtoIn {

    private Long id;
    private Long userId;
    private Long requestId;
    private String name;
    private String description;
    private Boolean available;
    private Long lastBookingId;
    private Long nextBookingId;
}
