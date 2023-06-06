package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoForOwner;
import ru.practicum.shareit.comment.dto.CommentDto;

import java.util.List;

@Data
@Builder
public class ItemDtoWithBookingAndComments {
    private long id;
    private long owner;
    private String name;
    private String description;
    private Boolean available;
    private BookingDtoForOwner lastBooking;
    private BookingDtoForOwner nextBooking;
    private List<CommentDto> comments;
}