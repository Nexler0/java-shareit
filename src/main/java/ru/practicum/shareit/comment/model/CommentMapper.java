package ru.practicum.shareit.comment.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    public static List<CommentShort> toCommentShort(List<Comment> comments) {
        List<CommentShort> result = new ArrayList<>();

        for (Comment comment : comments) {
            result.add(new CommentShort(comment.getId(), comment.getItem().getId(),
                    comment.getAuthor().getName(), comment.getText(), LocalDateTime.now()));
        }
        return result;
    }

    public static CommentShort toCommentShort(Comment comment) {
        return new CommentShort(comment.getId(), comment.getItem().getId(),
                comment.getAuthor().getName(), comment.getText(), LocalDateTime.now());

    }
}
