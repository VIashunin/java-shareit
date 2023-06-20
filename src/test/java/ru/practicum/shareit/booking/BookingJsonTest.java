package ru.practicum.shareit.booking;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingShort;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingJsonTest {
    @Autowired
    private JacksonTester<BookingShort> jsonBookingShort;

    @Test
    @SneakyThrows
    void testBookingShort() {
        LocalDateTime start = LocalDateTime.of(2024, 6, 16, 19, 01, 13);
        LocalDateTime end = LocalDateTime.of(2024, 6, 16, 20, 01, 13);
        BookingShort bookingShort = BookingShort.builder().start(start).end(end).itemId(1L).build();

        JsonContent<BookingShort> result = jsonBookingShort.write(bookingShort);

        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(start.toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(end.toString());
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }
}
