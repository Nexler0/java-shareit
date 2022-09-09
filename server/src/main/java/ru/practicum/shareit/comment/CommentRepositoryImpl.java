package ru.practicum.shareit.comment;

import org.springframework.context.annotation.Lazy;

public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final CommentRepository commentRepository;

    public CommentRepositoryImpl(@Lazy CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
}
