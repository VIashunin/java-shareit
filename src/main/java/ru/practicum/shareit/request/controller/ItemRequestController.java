package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.constants.Constants;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    ItemRequestDto addRequest(@RequestHeader(Constants.X_SHARER_USER_ID) long userId, @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.addRequest(userId, itemRequestDto);
    }

    @GetMapping
    List<ItemRequestDto> getRequest(@RequestHeader(Constants.X_SHARER_USER_ID) long userId) {
        return itemRequestService.getRequest(userId);
    }

    @GetMapping("/all")
    List<ItemRequestDto> getAllRequests(@RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                        @RequestParam(required = false) Integer from,
                                        @RequestParam(required = false) Integer size) {
        return itemRequestService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    ItemRequestDto getRequestById(@RequestHeader(Constants.X_SHARER_USER_ID) long userId, @PathVariable long requestId) {
        return itemRequestService.getRequestById(userId, requestId);
    }
}
