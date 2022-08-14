package ru.practicum.shareit.comment;

import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDtoOut;

public interface CommentService {
    ItemDtoOut addComment(Long userId, Long itemId, Comment comment);
}
