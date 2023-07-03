package ru.practicum.server.request.service;

import ru.practicum.server.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addRequest(long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getRequest(long userId);

    List<ItemRequestDto> getAllRequests(long userId, Integer from, Integer size);

    ItemRequestDto getRequestById(long userId, long requestId);
}
