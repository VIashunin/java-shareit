package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDtoForOwner;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {
    public ItemDto mapFromItemToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner().getId())
                .build();
    }

    public List<ItemDto> mapFromItemListToItemDtoList(List<Item> listItem) {
        List<ItemDto> listItemDto = new ArrayList<>();
        for (Item item : listItem) {
            listItemDto.add(ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .owner(item.getOwner().getId())
                    .build());
        }
        return listItemDto;
    }

    public Item mapFromItemDtoToItem(ItemDto itemDto, User user) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(user)
                .build();
    }

    public ItemDto mapFromItemDtoWithBookingAndCommentsToItemDto(ItemDtoWithBookingAndComments itemDtoWithBookingAndComments) {
        return ItemDto.builder()
                .id(itemDtoWithBookingAndComments.getId())
                .owner(itemDtoWithBookingAndComments.getOwner())
                .name(itemDtoWithBookingAndComments.getName())
                .description(itemDtoWithBookingAndComments.getDescription())
                .available(itemDtoWithBookingAndComments.getAvailable())
                .build();
    }

    public ItemDtoWithBookingAndComments mapToItemDtoWithBookingAndComments(Item item, BookingDtoForOwner bookingLast,
                                                                            BookingDtoForOwner bookingNext, List<CommentDto> comments) {
        return ItemDtoWithBookingAndComments.builder()
                .id(item.getId())
                .owner(item.getOwner().getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(bookingLast)
                .nextBooking(bookingNext)
                .comments(comments)
                .build();
    }

    public List<ItemDtoWithBookingAndComments> mapToItemDtoWithBookingAndCommentsList(List<Item> itemList, HashMap<Long,
            BookingDtoForOwner> bookingsLast, HashMap<Long, BookingDtoForOwner> bookingsNext, HashMap<Long, List<CommentDto>> comments) {
        List<ItemDtoWithBookingAndComments> itemDtoWithBookingList = new ArrayList<>();
        for (Item item : itemList) {
            BookingDtoForOwner bookingLast = bookingsLast.get(item.getId());
            BookingDtoForOwner bookingNext = bookingsNext.get(item.getId());
            List<CommentDto> commentList = comments.get(item.getId());
            itemDtoWithBookingList.add(mapToItemDtoWithBookingAndComments(item, bookingLast, bookingNext, commentList));
        }
        return itemDtoWithBookingList.stream()
                .sorted(Comparator.comparing(ItemDtoWithBookingAndComments::getId))
                .collect(Collectors.toList());
    }
}
