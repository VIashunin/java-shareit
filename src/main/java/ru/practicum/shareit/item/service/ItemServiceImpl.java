package ru.practicum.shareit.item.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.request.service.ItemRequestServiceImpl.getPageable;

@Service
@Transactional
@NoArgsConstructor
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ItemRequestService itemRequestService;

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        User user = userService.findUserById(userId);
        ItemRequest itemRequest = null;
        if (itemDto.getRequestId() != null) {
            itemRequest = ItemRequestMapper.mapFromItemRequestDtoToItemRequest(itemRequestService.getRequestById(userId, itemDto.getRequestId()), user);
        }
        return ItemMapper.mapFromItemToItemDto(itemRepository.save(ItemMapper.mapFromItemDtoToItem(itemDto, user, itemRequest)));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        userService.findUserById(userId);
        ItemDto itemDtoForUpdate = ItemMapper.mapFromItemDtoWithBookingAndCommentsToItemDto(getItemById(userId, itemId));
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
        BookingDtoForOwner bookingLast = BookingMapper.mapToBookingDtoForOwner(bookingRepository.findByItemIdLast(userId, itemId, LocalDateTime.now()));
        BookingDtoForOwner bookingNext = BookingMapper.mapToBookingDtoForOwner(bookingRepository.findByItemIdNext(userId, itemId, LocalDateTime.now()));
        List<CommentDto> comments = commentService.getCommentsByItemId(itemId);
        return ItemMapper.mapToItemDtoWithBookingAndComments(item, bookingLast, bookingNext, comments);
    }

    @Override
    public List<ItemDtoWithBookingAndComments> getAllItemsByUserId(long userId, Integer from, Integer size) {
        Pageable page = paginate(from, size);
        HashMap<Long, BookingDtoForOwner> bookingsLast = new HashMap<>();
        HashMap<Long, BookingDtoForOwner> bookingsNext = new HashMap<>();
        HashMap<Long, List<CommentDto>> comments = new HashMap<>();
        List<Item> items = itemRepository.findByOwnerId(userId, page);
        for (Item i : items) {
            bookingsLast.put(i.getId(), BookingMapper.mapToBookingDtoForOwner(bookingRepository.findByItemIdLast(userId, i.getId(), LocalDateTime.now())));
            bookingsNext.put(i.getId(), BookingMapper.mapToBookingDtoForOwner(bookingRepository.findByItemIdNext(userId, i.getId(), LocalDateTime.now())));
            comments.put(i.getId(), commentService.getCommentsByItemId(i.getId()));
        }
        return ItemMapper.mapToItemDtoWithBookingAndCommentsList(items, bookingsLast, bookingsNext, comments);
    }

    @Override
    public List<ItemDto> searchAvailableItem(String text, Integer from, Integer size) {
        if (text.isEmpty()) {
            return List.of();
        } else {
            Pageable page = paginate(from, size);
            return ItemMapper.mapFromItemListToItemDtoList(itemRepository.searchAvailableItem(text, page));
        }
    }

    @Override
    public Item findItemById(long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    public Pageable paginate(Integer from, Integer size) {
        return getPageable(from, size);
    }
}
