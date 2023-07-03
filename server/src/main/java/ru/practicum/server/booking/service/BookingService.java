package ru.practicum.server.booking.service;

import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.booking.dto.BookingShort;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(long userId, BookingShort bookingShort);

    BookingDto confirmBooking(long userId, long bookingId, boolean approved);

    BookingDto getBookingById(long userId, long bookingId);

    List<BookingDto> getBookingsListByUserId(long userId, String state, Integer from, Integer size);

    List<BookingDto> getBookingsListByOwnerId(long userId, String state, Integer from, Integer size);
}
