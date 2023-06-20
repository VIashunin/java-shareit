package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = "spring.datasource.username=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    private final EntityManager em;
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    @Test
    public void addBookingTest() {
        LocalDateTime start = LocalDateTime.of(2024, 7, 15, 21, 15, 21);
        LocalDateTime end = LocalDateTime.of(2024, 7, 15, 21, 16, 21);
        User user = User.builder().id(1L).name("User").email("user@yandex.ru").build();
        User owner = User.builder().id(2L).name("Owner").email("owner@yandex.ru").build();
        UserDto userDto = UserDto.builder().id(1L).name("User").email("user@yandex.ru").build();
        UserDto userOwnerDto = UserDto.builder().id(2L).name("Owner").email("owner@yandex.ru").build();
        Item item = Item.builder().id(1L).owner(owner).name("Item").description("Item").available(true).request(null).build();
        ItemDto itemDto = ItemDto.builder().name("Item").description("Item").available(true).owner(owner.getId()).requestId(null).build();
        BookingShort bookingShort = BookingShort.builder().start(start).end(end).itemId(item.getId()).build();
        BookingDto bookingDto = BookingDto.builder().start(start).end(end).status(BookingStatus.WAITING).booker(user).item(item).build();

        userService.addUser(userDto);
        userService.addUser(userOwnerDto);
        itemService.addItem(userOwnerDto.getId(), itemDto);
        bookingService.addBooking(userDto.getId(), bookingShort);

        TypedQuery<Booking> query = em.createQuery("Select u from Booking u where u.booker.id = :bookerId", Booking.class);
        Booking booking = query.setParameter("bookerId", user.getId()).getSingleResult();

        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getStart(), equalTo(bookingDto.getStart()));
        assertThat(booking.getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(booking.getItem(), equalTo(bookingDto.getItem()));
        assertThat(booking.getBooker(), equalTo(bookingDto.getBooker()));
        assertThat(booking.getBookingStatus(), equalTo(bookingDto.getStatus()));
    }

    @Test
    public void confirmBookingTest() {
        LocalDateTime start = LocalDateTime.of(2024, 7, 15, 21, 15, 21);
        LocalDateTime end = LocalDateTime.of(2024, 7, 15, 21, 16, 21);
        User user = User.builder().id(3L).name("User").email("user@yandex.ru").build();
        User owner = User.builder().id(4L).name("Owner").email("owner@yandex.ru").build();
        UserDto userDto = UserDto.builder().id(1L).name("User").email("user@yandex.ru").build();
        UserDto userOwnerDto = UserDto.builder().id(4L).name("Owner").email("owner@yandex.ru").build();
        Item item = Item.builder().id(2L).owner(owner).name("Item").description("Item").available(true).request(null).build();
        ItemDto itemDto = ItemDto.builder().name("Item").description("Item").available(true).owner(owner.getId()).requestId(null).build();
        BookingDto bookingDto = BookingDto.builder().start(start).end(end).status(BookingStatus.WAITING).booker(user).item(item).build();

        UserDto userDtoDb = userService.addUser(userDto);
        UserDto ownerDtoDb = userService.addUser(userOwnerDto);
        ItemDto itemDtoDb = itemService.addItem(ownerDtoDb.getId(), itemDto);
        BookingShort bookingShort = BookingShort.builder().start(start).end(end).itemId(itemDtoDb.getId()).build();
        BookingDto bookingDtoDb = bookingService.addBooking(userDtoDb.getId(), bookingShort);
        bookingService.confirmBooking(ownerDtoDb.getId(), bookingDtoDb.getId(), true);

        TypedQuery<Booking> query = em.createQuery("Select u from Booking u where u.booker.id = :bookerId", Booking.class);
        Booking booking = query.setParameter("bookerId", userDtoDb.getId()).getSingleResult();

        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getStart(), equalTo(bookingDto.getStart()));
        assertThat(booking.getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(booking.getItem(), equalTo(bookingDto.getItem()));
        assertThat(booking.getBooker(), equalTo(bookingDto.getBooker()));
        assertThat(booking.getBookingStatus(), equalTo(BookingStatus.APPROVED));
    }
}
