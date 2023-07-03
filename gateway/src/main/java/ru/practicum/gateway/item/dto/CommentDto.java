package ru.practicum.gateway.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    @NotBlank
    private String text;
    private LocalDateTime created;
}
