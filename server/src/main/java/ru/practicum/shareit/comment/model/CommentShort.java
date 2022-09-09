package ru.practicum.shareit.comment.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class CommentShort {

    @EqualsAndHashCode.Exclude
    private Long id;
    private Long itemId;
    private String authorName;
    private Long authorId;
    private String text;
    private LocalDateTime created;
}
