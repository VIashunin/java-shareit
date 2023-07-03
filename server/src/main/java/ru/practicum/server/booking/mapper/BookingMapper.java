package ru.practicum.server.booking.mapper;

import ru.practicum.server.booking.dto.BookingDto;
import ru.practicum.server.booking.dto.BookingDtoForOwner;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {
    public static Booking mapFromBookingDtoToBooking(BookingDto bookingDto, Item item, User user) {
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .booker(user)
                .bookingStatus(bookingDto.getStatus())
                .build();
    }

    public static BookingDto mapFromBookingToBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getBookingStatus())
                .build();
    }

    public static List<BookingDto> mapFromBookingListToBookingDtoList(List<Booking> bookingList) {
        List<BookingDto> bookingDtoList = new ArrayList<>();
        for (Booking booking : bookingList) {
            bookingDtoList.add(mapFromBookingToBookingDto(booking));
        }
        return bookingDtoList;
    }

    public static BookingDtoForOwner mapToBookingDtoForOwner(Booking booking) {
        if (booking != null) {
            return BookingDtoForOwner.builder()
                    .id(booking.getId())
                    .start(booking.getStart())
                    .end(booking.getEnd())
                    .bookerId(booking.getBooker().getId())
                    .status(booking.getBookingStatus())
                    .build();
        } else {
            return null;
        }
    }
}
