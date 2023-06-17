package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addRequest(long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getRequest(long userId);

    List<ItemRequestDto> getAllRequests(long userId, Integer from, Integer size);

    ItemRequestDto getRequestById(long userId, long requestId);
}
