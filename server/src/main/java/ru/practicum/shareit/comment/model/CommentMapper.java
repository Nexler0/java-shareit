package ru.practicum.shareit.comment.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public static List<CommentShort> toCommentShort(List<Comment> comments) {
        List<CommentShort> result = new ArrayList<>();

        for (Comment comment : comments) {
            result.add(new CommentShort(comment.getId(), comment.getItem().getId(),
                    comment.getAuthor().getName(), comment.getAuthor().getId(), comment.getText(),
                    LocalDateTime.now().withNano(0)));
        }
        return result;
    }

    public static CommentShort toCommentShort(Comment comment) {
        return new CommentShort(comment.getId(), comment.getItem().getId(),
                comment.getAuthor().getName(), comment.getAuthor().getId(), comment.getText(),
                LocalDateTime.now().withNano(0));

    }

    public Comment toComment(CommentShort commentShort) {
        Comment comment = new Comment();
        comment.setText(commentShort.getText());
        comment.setAuthor(userRepository.getUserById(commentShort.getAuthorId()));
        comment.setItem(itemRepository.getItemById(commentShort.getItemId()));
        comment.setId(commentShort.getId());
        return comment;
    }
}
