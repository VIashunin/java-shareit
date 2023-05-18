package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMapper {
    public ItemDto mapFromItemToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(Boolean.parseBoolean(item.getAvailable()))
                .owner(item.getOwner())
                .build();
    }

    public List<ItemDto> mapFromItemListToItemDtoList(List<Item> listItem) {
        List<ItemDto> listItemDto = new ArrayList<>();
        for (Item item : listItem) {
            listItemDto.add(ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(Boolean.parseBoolean(item.getAvailable()))
                    .owner(item.getOwner())
                    .build());
        }
        return listItemDto;
    }

    public Item mapFromItemDtoToItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(String.valueOf(itemDto.getAvailable()))
                .owner(itemDto.getOwner())
                .build();
    }
}
