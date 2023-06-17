package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentShort;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.constants.Constants;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    public ItemDto addItem(@RequestHeader(Constants.X_SHARER_USER_ID) long userId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader(Constants.X_SHARER_USER_ID) long userId, @PathVariable long itemId,
                              @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBookingAndComments getItemById(@NotNull @RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                                     @PathVariable long itemId) {
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemDtoWithBookingAndComments> getAllItemsByUserId(@RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                                                   @RequestParam(required = false) Integer from,
                                                                   @RequestParam(required = false) Integer size) {
        return itemService.getAllItemsByUserId(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchAvailableItem(@NotBlank @RequestParam String text,
                                             @RequestParam(required = false) Integer from,
                                             @RequestParam(required = false) Integer size) {
        return itemService.searchAvailableItem(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(Constants.X_SHARER_USER_ID) long userId, @PathVariable long itemId,
                                 @Valid @RequestBody CommentShort commentShort) {
        return commentService.addComment(userId, itemId, commentShort);
    }
}
