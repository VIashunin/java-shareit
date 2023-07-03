package ru.practicum.server.request.mapper;

import ru.practicum.server.request.dto.ItemRequestDto;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class ItemRequestMapper {
    public static ItemRequest mapFromItemRequestDtoToItemRequest(ItemRequestDto itemRequestDto, User user) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requestor(user)
                .created(itemRequestDto.getCreated())
                .build();
    }

    public static ItemRequestDto mapFromItemRequestToItemRequestDto(ItemRequest itemRequest) {
        if (itemRequest != null) {
            return ItemRequestDto.builder()
                    .id(itemRequest.getId())
                    .description(itemRequest.getDescription())
                    .requestor(itemRequest.getRequestor().getId())
                    .created(itemRequest.getCreated())
                    .build();
        } else {
            return null;
        }
    }

    public static List<ItemRequestDto> mapFromItemRequestListToItemRequestDtoList(List<ItemRequest> itemRequestList) {
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequestList) {
            itemRequestDtoList.add(mapFromItemRequestToItemRequestDto(itemRequest));
        }
        return itemRequestDtoList;
    }
}
