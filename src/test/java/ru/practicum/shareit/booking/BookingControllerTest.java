package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    static User user;
    static User owner;
    static Item item;
    static BookingDto bookingDto;
    static BookingShort bookingShort;
    static List<BookingDto> bookingDtoList;
    static LocalDateTime start;
    static LocalDateTime end;
    static String X_SHARER_USER_ID = "X-Sharer-User-Id";

    @BeforeEach
    void beforeEach() {
        start = LocalDateTime.of(2024, 6, 15, 21, 15, 21);
        end = LocalDateTime.of(2024, 6, 15, 21, 16, 21);
        user = User.builder().id(1L).name("User").email("user@yandex.ru").build();
        owner = User.builder().id(2L).name("Owner").email("owner@yandex.ru").build();
        item = Item.builder().id(1L).owner(owner).name("Item").description("Item").available(true).request(null).build();
        bookingDto = BookingDto.builder().start(start).end(end).status(BookingStatus.WAITING).booker(user).item(item).build();
        bookingShort = BookingShort.builder().start(start).end(end).itemId(item.getId()).build();
        bookingDtoList = List.of(BookingDto.builder().start(start).end(end).status(BookingStatus.WAITING).booker(user).item(item).build());
    }

    @SneakyThrows
    void andExpectBookingDto(ResultActions resultActions, BookingDto bookingDto) {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString())))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().name())))
                .andExpect(jsonPath("$.booker", notNullValue()))
                .andExpect(jsonPath("$.item", notNullValue()));
    }

    @SneakyThrows
    void andExpectBookingDtoList(ResultActions resultActions, List<BookingDto> bookingDtoList) {
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDtoList)));
    }

    @Test
    @SneakyThrows
    void addBookingTest() {
        when(bookingService.addBooking(anyLong(), any()))
                .thenReturn(bookingDto);

        ResultActions ra = mvc.perform(post("/bookings")
                .content(mapper.writeValueAsString(bookingShort))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_SHARER_USER_ID, 1L));
        andExpectBookingDto(ra, bookingDto);
    }

    @Test
    @SneakyThrows
    void confirmBookingTest() {
        when(bookingService.confirmBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        ResultActions ra = mvc.perform(patch("/bookings/1?approved=true")
                .content(mapper.writeValueAsString(bookingDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_SHARER_USER_ID, 1L));
        andExpectBookingDto(ra, bookingDto);
    }

    @Test
    @SneakyThrows
    void getBookingByIdTest() {
        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        ResultActions ra = mvc.perform(get("/bookings/1")
                .content(mapper.writeValueAsString(bookingDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_SHARER_USER_ID, 1L));
        andExpectBookingDto(ra, bookingDto);
    }

    @Test
    @SneakyThrows
    void getBookingsListByUserIdTest() {
        when(bookingService.getBookingsListByUserId(anyLong(), any(), any(), any()))
                .thenReturn(bookingDtoList);

        ResultActions ra = mvc.perform(get("/bookings")
                .content(mapper.writeValueAsString(bookingDtoList))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_SHARER_USER_ID, 1L));
        andExpectBookingDtoList(ra, bookingDtoList);
    }

    @Test
    @SneakyThrows
    void getBookingsListByOwnerIdTest() {
        when(bookingService.getBookingsListByOwnerId(anyLong(), any(), any(), any()))
                .thenReturn(bookingDtoList);

        ResultActions ra = mvc.perform(get("/bookings/owner")
                .content(mapper.writeValueAsString(bookingDtoList))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(X_SHARER_USER_ID, 1L));
        andExpectBookingDtoList(ra, bookingDtoList);
    }
}