package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto addItem(long userId, Item item);

    ItemDto updateItem(long userId, long itemId, Item item);

    ItemDto getItemById(long itemId);

    List<ItemDto> getAllItemsByUserId(long userId);

    List<ItemDto> searchItem(String text);
}
