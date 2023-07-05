package ru.practicum.gateway.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@NotNull @RequestHeader(X_SHARER_USER_ID) long userId,
                                             @RequestBody @Valid ItemRequestDto itemRequestDto) {
        return itemRequestClient.addRequest(userId, itemRequestDto);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@NotNull @RequestHeader(X_SHARER_USER_ID) long userId,
                                             @PathVariable long requestId) {
        return itemRequestClient.getRequest(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequests(@NotNull @RequestHeader(X_SHARER_USER_ID) long userId,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                              @Positive @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestClient.getRequests(userId, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestToRequestor(@NotNull @RequestHeader(X_SHARER_USER_ID) long userId) {
        return itemRequestClient.getRequestToRequestor(userId);
    }
}
