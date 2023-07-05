package ru.practicum.server.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.server.booking.status.BookingStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoForOwner {
    private long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private long bookerId;
    private BookingStatus status;
}
