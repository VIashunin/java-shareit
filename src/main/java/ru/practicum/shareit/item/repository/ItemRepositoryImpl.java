package ru.practicum.shareit.item.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.NotOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final ItemMapper itemMapper;
    private final UserRepository userRepository;
    private final HashMap<Long, List<Item>> items;
    private long id;

    @Autowired
    public ItemRepositoryImpl(ItemMapper itemMapper, UserRepository userRepository) {
        this.itemMapper = itemMapper;
        this.userRepository = userRepository;
        items = new HashMap<>();
        id = 1;
    }

    @Override
    public ItemDto addItem(long userId, Item item) {
        userRepository.getUserById(userId);
        item.setId(id++);
        item.setOwner(userId);
        List<Item> userItems;
        if (items.get(userId) == null) {
            userItems = new ArrayList<>();
        } else {
            userItems = items.get(userId);
        }
        userItems.add(item);
        items.put(userId, userItems);
        return itemMapper.mapFromItemToItemDto(item);
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, Item item) {
        item.setOwner(userId);
        items.values().stream()
                .flatMap(Collection::stream)
                .forEach(oldItem -> {
                    if (oldItem.getId() == itemId) {
                        validateOwner(oldItem, item);
                        if (item.getName() != null) {
                            oldItem.setName(item.getName());
                        }
                        if (item.getDescription() != null) {
                            oldItem.setDescription(item.getDescription());
                        }
                        if (!item.getAvailable().equals("null")) {
                            oldItem.setAvailable(item.getAvailable());
                        }
                    }
                });
        return getItemById(itemId);
    }

    private void validateOwner(Item oldItem, Item item) {
        if (oldItem.getOwner() != item.getOwner()) {
            throw new NotOwnerException("User with id: " + item.getOwner() + " is not owner of the item with id: " + oldItem.getId() + ".");
        }
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return itemMapper.mapFromItemToItemDto(items.values().stream()
                .flatMap(Collection::stream)
                .filter(item -> item.getId() == itemId)
                .findFirst()
                .orElseThrow(() -> new ItemNotFoundException(itemId)));
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(long userId) {
        if (items.get(userId).isEmpty() || items.get(userId) == null) {
            return itemMapper.mapFromItemListToItemDtoList(Collections.emptyList());
        } else {
            return itemMapper.mapFromItemListToItemDtoList(items.get(userId));
        }
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.isEmpty() || text.isBlank()) {
            return itemMapper.mapFromItemListToItemDtoList(Collections.emptyList());
        } else {
            return itemMapper.mapFromItemListToItemDtoList(items.values().stream()
                    .flatMap(Collection::stream)
                    .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                            || item.getDescription().toLowerCase().contains(text.toLowerCase())
                            && item.getAvailable().equals("true")))
                    .collect(Collectors.toList()));
        }
    }
}
