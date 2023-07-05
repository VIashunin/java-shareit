package ru.practicum.gateway.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.item.dto.CommentDto;
import ru.practicum.gateway.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(X_SHARER_USER_ID) long userId,
                                          @Valid @RequestBody ItemDto itemDtoGateway) {
        return itemClient.addItem(userId, itemDtoGateway);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@NotNull @RequestHeader(X_SHARER_USER_ID) long userId,
                                             @PathVariable long itemId,
                                             @RequestBody @Valid CommentDto commentDtoGateway) {
        return itemClient.addComment(userId, itemId, commentDtoGateway);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@NotNull @RequestHeader(X_SHARER_USER_ID) long userId,
                                             @PathVariable long itemId,
                                             @RequestBody ItemDto itemDtoGateway) {
        return itemClient.updateItem(userId, itemId, itemDtoGateway);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam String text,
                                             @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                             @Positive @RequestParam(defaultValue = "10") Integer size) {
        return itemClient.searchItem(text, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@NotNull @RequestHeader(X_SHARER_USER_ID) long userId,
                                          @PathVariable long itemId) {
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@NotNull @RequestHeader(X_SHARER_USER_ID) long userId,
                                           @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                           @Positive @RequestParam(defaultValue = "10") Integer size) {
        return itemClient.getItems(userId, from, size);
    }
}
