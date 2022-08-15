package ru.practicum.shareit.comment;

import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.model.CommentShort;

public interface CommentService {
    CommentShort addComment(Long userId, Long itemId, Comment comment);
}
