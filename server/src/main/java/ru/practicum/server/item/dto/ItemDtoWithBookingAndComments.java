package ru.practicum.server.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.server.booking.dto.BookingDtoForOwner;
import ru.practicum.server.comment.dto.CommentDto;

import java.util.List;

@Data
@Builder
public class ItemDtoWithBookingAndComments {
    private long id;
    private Long owner;
    private String name;
    private String description;
    private Boolean available;
    private Long request;
    private BookingDtoForOwner lastBooking;
    private BookingDtoForOwner nextBooking;
    private List<CommentDto> comments;
}
