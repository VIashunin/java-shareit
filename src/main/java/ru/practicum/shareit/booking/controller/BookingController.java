package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.constants.Constants;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    BookingDto addBooking(@RequestHeader(Constants.X_SHARER_USER_ID) long userId, @Valid @RequestBody BookingShort bookingShort) {
        return bookingService.addBooking(userId, bookingShort);
    }

    @PatchMapping("/{bookingId}")
    BookingDto confirmBooking(@RequestHeader(Constants.X_SHARER_USER_ID) long userId, @PathVariable long bookingId,
                              @RequestParam boolean approved) {
        return bookingService.confirmBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    BookingDto getBookingById(@RequestHeader(Constants.X_SHARER_USER_ID) long userId, @PathVariable long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    List<BookingDto> getBookingsListByUserId(@RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                             @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.getBookingsListByUserId(userId, state);
    }

    @GetMapping("/owner")
    List<BookingDto> getBookingsListByOwnerId(@RequestHeader(Constants.X_SHARER_USER_ID) long userId,
                                              @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.getBookingsListByOwnerId(userId, state);
    }
}
