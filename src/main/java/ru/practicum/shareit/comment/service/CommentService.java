package ru.practicum.shareit.comment.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentShort;

import java.util.List;

public interface CommentService {
    CommentDto addComment(long userId, long itemId, CommentShort commentShort);

    List<CommentDto> getCommentsByItemId(long itemId);
}
