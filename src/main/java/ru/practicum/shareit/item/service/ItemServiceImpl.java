package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemDto addItem(long userId, Item item) {
        return itemRepository.addItem(userId, item);
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, Item item) {
        return itemRepository.updateItem(userId, itemId, item);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return itemRepository.getItemById(itemId);
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(long userId) {
        return itemRepository.getAllItemsByUserId(userId);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        return itemRepository.searchItem(text);
    }
}
