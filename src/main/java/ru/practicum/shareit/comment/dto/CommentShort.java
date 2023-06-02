package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentShort {
    @NotBlank
    @NotEmpty
    private String text;
    private LocalDateTime created;
}
