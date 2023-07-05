package ru.practicum.server.item.service;

import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.server.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    ItemDtoWithBookingAndComments getItemById(long userId, long itemId);

    List<ItemDtoWithBookingAndComments> getAllItemsByUserId(long userId, Integer from, Integer size);

    List<ItemDto> searchAvailableItem(String text, Integer from, Integer size);

    Item findItemById(long itemId);
}
