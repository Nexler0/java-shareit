package ru.practicum.shareit.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.practicum.shareit.comment.model.Comment;

import java.util.List;

@RepositoryRestResource(path = "comments")
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    @Query("select c from Comment c where c.item.id = ?1")
    List<Comment> getAllByItemId(Long itemId);

    @Query("select (count(c) > 0) from Comment c where c.item.id = ?1")
    Boolean existsCommentByItemId(Long itemId);
}
