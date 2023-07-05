package ru.practicum.server.comment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentShort {
    private String text;
    private LocalDateTime created;
}
