package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestDto {
    private long id;
    @NotBlank
    @NotEmpty
    private String description;
    private LocalDateTime created;
    private long requestor;
    private List<ItemDto> items;
}
