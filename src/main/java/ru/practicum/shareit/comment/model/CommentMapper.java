package ru.practicum.shareit.comment.model;

import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    public static List<CommentShort> toCommentShort(List<Comment> comments) {
        List<CommentShort> result = new ArrayList<>();
        for(Comment comment : comments){
            result.add(new CommentShort(comment.getId(), comment.getItem().getId(),
                    comment.getAuthor().getId(), comment.getText()));
        }
        return result;
    }
}
