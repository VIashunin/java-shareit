package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingDtoForOwner;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemDto mapFromItemToItemDto(Item item) {
        if (item.getRequest() != null) {
            return ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.isAvailable())
                    .owner(item.getOwner().getId())
                    .requestId(item.getRequest().getId())
                    .build();
        } else {
            return ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.isAvailable())
                    .owner(item.getOwner().getId())
                    .requestId(null)
                    .build();
        }
    }

    public static List<ItemDto> mapFromItemListToItemDtoList(List<Item> listItem) {
        List<ItemDto> listItemDto = new ArrayList<>();
        for (Item item : listItem) {
            listItemDto.add(mapFromItemToItemDto(item));
        }
        return listItemDto;
    }

    public static Item mapFromItemDtoToItem(ItemDto itemDto, User user, ItemRequest itemRequest) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(user)
                .request(itemRequest)
                .build();
    }

    public static ItemDto mapFromItemDtoWithBookingAndCommentsToItemDto(ItemDtoWithBookingAndComments itemDtoWithBookingAndComments) {
        return ItemDto.builder()
                .id(itemDtoWithBookingAndComments.getId())
                .owner(itemDtoWithBookingAndComments.getOwner())
                .name(itemDtoWithBookingAndComments.getName())
                .description(itemDtoWithBookingAndComments.getDescription())
                .available(itemDtoWithBookingAndComments.getAvailable())
                .build();
    }

    public static ItemDtoWithBookingAndComments mapToItemDtoWithBookingAndComments(Item item, BookingDtoForOwner bookingLast,
                                                                                   BookingDtoForOwner bookingNext, List<CommentDto> comments) {
        return ItemDtoWithBookingAndComments.builder()
                .id(item.getId())
                .owner(item.getOwner().getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .lastBooking(bookingLast)
                .nextBooking(bookingNext)
                .comments(comments)
                .build();
    }

    public static List<ItemDtoWithBookingAndComments> mapToItemDtoWithBookingAndCommentsList(List<Item> itemList, HashMap<Long,
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
