package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(long userId, BookingShort bookingShort);

    BookingDto confirmBooking(long userId, long bookingId, boolean approved);

    BookingDto getBookingById(long userId, long bookingId);

    List<BookingDto> getBookingsListByUserId(long userId, String state);

    List<BookingDto> getBookingsListByOwnerId(long userId, String state);
}