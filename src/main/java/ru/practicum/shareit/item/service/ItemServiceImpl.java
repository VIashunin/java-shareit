package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoForOwner;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CommentService commentService;

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        User user = userService.findUserById(userId);
        return itemMapper.mapFromItemToItemDto(itemRepository.save(itemMapper.mapFromItemDtoToItem(itemDto, user)));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        userService.findUserById(userId);
        ItemDto itemDtoForUpdate = itemMapper.mapFromItemDtoWithBookingAndCommentsToItemDto(getItemById(userId, itemId));
        if (!Objects.isNull(itemDto.getName())) {
            itemDtoForUpdate.setName(itemDto.getName());
        }
        if (!Objects.isNull(itemDto.getDescription())) {
            itemDtoForUpdate.setDescription(itemDto.getDescription());
        }
        if (!Objects.isNull(itemDto.getAvailable())) {
            itemDtoForUpdate.setAvailable(itemDto.getAvailable());
        }
        return addItem(userId, itemDtoForUpdate);
    }

    @Override
    public ItemDtoWithBookingAndComments getItemById(long userId, long itemId) {
        Item item = findItemById(itemId);
        BookingDtoForOwner bookingLast = bookingMapper.mapToBookingDtoForOwner(bookingRepository.findByItemIdLast(userId, itemId, LocalDateTime.now()));
        BookingDtoForOwner bookingNext = bookingMapper.mapToBookingDtoForOwner(bookingRepository.findByItemIdNext(userId, itemId, LocalDateTime.now()));
        List<CommentDto> comments = commentService.getCommentsByItemId(itemId);
        return itemMapper.mapToItemDtoWithBookingAndComments(item, bookingLast, bookingNext, comments);
    }

    @Override
    public List<ItemDtoWithBookingAndComments> getAllItemsByUserId(long userId) {
        HashMap<Long, BookingDtoForOwner> bookingsLast = new HashMap<>();
        HashMap<Long, BookingDtoForOwner> bookingsNext = new HashMap<>();
        HashMap<Long, List<CommentDto>> comments = new HashMap<>();
        List<Item> items = itemRepository.findByOwnerId(userId);
        for (Item i : items) {
            bookingsLast.put(i.getId(), bookingMapper.mapToBookingDtoForOwner(bookingRepository.findByItemIdLast(userId, i.getId(), LocalDateTime.now())));
            bookingsNext.put(i.getId(), bookingMapper.mapToBookingDtoForOwner(bookingRepository.findByItemIdNext(userId, i.getId(), LocalDateTime.now())));
            comments.put(i.getId(), commentService.getCommentsByItemId(i.getId()));
        }
        return itemMapper.mapToItemDtoWithBookingAndCommentsList(items, bookingsLast, bookingsNext, comments);
    }

    @Override
    public List<ItemDto> searchAvailableItem(String text) {
        if (text.isEmpty()) {
            return List.of();
        } else {
            return itemMapper.mapFromItemListToItemDtoList(itemRepository.searchAvailableItem(text));
        }
    }

    @Override
    public Item findItemById(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
    }
}
