package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public BookingDto addBooking(@RequestHeader(X_SHARER_USER_ID) long userId, @Valid @RequestBody BookingShort bookingShort) {
        return bookingService.addBooking(userId, bookingShort);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto confirmBooking(@RequestHeader(X_SHARER_USER_ID) long userId, @PathVariable long bookingId,
                                     @RequestParam boolean approved) {
        return bookingService.confirmBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(X_SHARER_USER_ID) long userId, @PathVariable long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBookingsListByUserId(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                    @RequestParam(required = false, defaultValue = "ALL") String state,
                                                    @RequestParam(required = false) Integer from,
                                                    @RequestParam(required = false) Integer size) {
        return bookingService.getBookingsListByUserId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsListByOwnerId(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                     @RequestParam(required = false, defaultValue = "ALL") String state,
                                                     @RequestParam(required = false) Integer from,
                                                     @RequestParam(required = false) Integer size) {
        return bookingService.getBookingsListByOwnerId(userId, state, from, size);
    }
}
