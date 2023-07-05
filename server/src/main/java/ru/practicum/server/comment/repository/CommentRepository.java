package ru.practicum.server.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.server.comment.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "select c.* from comments as c " +
            "where c.item_id = ?1 ", nativeQuery = true)
    List<Comment> getCommentsByItemId(long itemId);
}
