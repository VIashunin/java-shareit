package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    ItemDtoWithBookingAndComments getItemById(long userId, long itemId);

    List<ItemDtoWithBookingAndComments> getAllItemsByUserId(long userId, Integer from, Integer size);

    List<ItemDto> searchAvailableItem(String text, Integer from, Integer size);

    Item findItemById(long itemId);
}
