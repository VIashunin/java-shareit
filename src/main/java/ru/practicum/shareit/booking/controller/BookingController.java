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

    @PostMapping
    BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody BookingShort bookingShort) {
        return bookingService.addBooking(userId, bookingShort);
    }

    @PatchMapping("/{bookingId}")
    BookingDto confirmBooking(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId, @RequestParam boolean approved) {
        return bookingService.confirmBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    List<BookingDto> getBookingsListByUserId(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.getBookingsListByUserId(userId, state);
    }

    @GetMapping("/owner")
    List<BookingDto> getBookingsListByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.getBookingsListByOwnerId(userId, state);
    }
}
