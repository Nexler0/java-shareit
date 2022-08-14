package ru.practicum.shareit.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentShort {
    private Long id;
    private Long itemId;
    private Long userId;
    private String text;
}
