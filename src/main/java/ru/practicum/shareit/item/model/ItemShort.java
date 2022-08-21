package ru.practicum.shareit.item.model;

import lombok.Data;

@Data
public class ItemShort {
    private Long id;
    private String name;
    private String description;
    private boolean available;
    private Long requestId;
    private Long ownerId;
}
