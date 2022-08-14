package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.model.CommentShort;

import java.util.List;

@Data
@Builder
public class ItemDtoOut {
    private Long id;
    private Long userId;
    private Long requestId;
    private String name;
    private String description;
    private Boolean available;
    private Booking lastBooking;
    private Booking nextBooking;
    private List<CommentShort> comments;
}
