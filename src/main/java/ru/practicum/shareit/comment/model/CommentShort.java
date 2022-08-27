package ru.practicum.shareit.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class CommentShort {

    private Long id;
    private Long itemId;
    private String authorName;
    private Long authorId;
    private String text;
    private LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentShort that = (CommentShort) o;
        return Objects.equals(itemId, that.itemId) && Objects.equals(authorName, that.authorName)
                && Objects.equals(authorId, that.authorId) && Objects.equals(text, that.text)
                && Objects.equals(created, that.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, authorName, authorId, text, created);
    }
}
