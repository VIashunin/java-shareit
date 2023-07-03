package ru.practicum.server.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.comment.dto.CommentDto;
import ru.practicum.server.comment.dto.CommentShort;
import ru.practicum.server.comment.service.CommentService;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.server.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto addItem(@RequestHeader(X_SHARER_USER_ID) long userId, @RequestBody ItemDto itemDto) {
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(X_SHARER_USER_ID) long userId, @PathVariable long itemId,
                              @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBookingAndComments getItemById(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                     @PathVariable long itemId) {
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemDtoWithBookingAndComments> getAllItemsByUserId(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                                   @RequestParam(required = false) Integer from,
                                                                   @RequestParam(required = false) Integer size) {
        return itemService.getAllItemsByUserId(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchAvailableItem(@RequestParam String text,
                                             @RequestParam(required = false) Integer from,
                                             @RequestParam(required = false) Integer size) {
        return itemService.searchAvailableItem(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(X_SHARER_USER_ID) long userId, @PathVariable long itemId,
                                 @RequestBody CommentShort commentShort) {
        return commentService.addComment(userId, itemId, commentShort);
    }
}
