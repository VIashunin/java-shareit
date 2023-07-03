package ru.practicum.server.comment.service;

import ru.practicum.server.comment.dto.CommentDto;
import ru.practicum.server.comment.dto.CommentShort;

import java.util.List;

public interface CommentService {
    CommentDto addComment(long userId, long itemId, CommentShort commentShort);

    List<CommentDto> getCommentsByItemId(long itemId);
}
