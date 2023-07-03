package ru.practicum.server.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.server.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestDto {
    private long id;
    private String description;
    private LocalDateTime created;
    private long requestor;
    private List<ItemDto> items;
}
