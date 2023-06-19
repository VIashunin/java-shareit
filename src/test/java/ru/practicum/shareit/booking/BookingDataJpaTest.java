package ru.practicum.shareit.booking;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
public class BookingDataJpaTest {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    LocalDateTime start = LocalDateTime.now();
    LocalDateTime end = LocalDateTime.now().plusMinutes(1);
    User user = User.builder().name("User").email("user@yandex.ru").build();
    User owner = User.builder().name("Owner").email("owner@yandex.ru").build();
    Item item = Item.builder().owner(owner).name("Item").description("Item").available(true).request(null).build();
    Booking booking = Booking.builder().start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build();

    @BeforeEach
    void dependencyInject() {
        userRepository.save(user);
        userRepository.save(owner);
        itemRepository.save(item);
        bookingRepository.save(booking);
    }

    @AfterEach
    void clearInject() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    void assertThat(Booking booking1, Booking booking2) {
        MatcherAssert.assertThat(booking1.getId(), notNullValue());
        MatcherAssert.assertThat(booking1.getStart(), equalTo(booking2.getStart()));
        MatcherAssert.assertThat(booking1.getEnd(), equalTo(booking2.getEnd()));
        MatcherAssert.assertThat(booking1.getItem(), equalTo(booking2.getItem()));
        MatcherAssert.assertThat(booking1.getBooker(), equalTo(booking2.getBooker()));
        MatcherAssert.assertThat(booking1.getBookingStatus(), equalTo(booking2.getBookingStatus()));
    }

    @Test
    void getBookingByIdTest() {
        Booking bookingJpa = bookingRepository.getBookingById(booking.getId(), user.getId());

        assertThat(bookingJpa, booking);
    }

    @Test
    void getAllBookingByUserIdTest() {
        List<Booking> bookingJpaList = bookingRepository.getAllBookingByUserId(user.getId(), Pageable.ofSize(4));

        assertThat(bookingJpaList.get(0), booking);
    }

    @Test
    void getBookingByUserIdAndBookingStatusCurrentTest() {
        List<Booking> bookingJpaList = bookingRepository.getBookingByUserIdAndBookingStatusCurrent(user.getId(), start.plusSeconds(5), Pageable.ofSize(4));

        assertThat(bookingJpaList.get(0), booking);
    }

    @Test
    void getBookingByUserIdAndBookingStatusPastTest() {

        List<Booking> bookingJpaList = bookingRepository.getBookingByUserIdAndBookingStatusPast(user.getId(), end.plusSeconds(5), Pageable.ofSize(4));

        assertThat(bookingJpaList.get(0), booking);
    }

    @Test
    void getBookingByUserIdAndBookingStatusFutureTest() {
        List<Booking> bookingJpaList = bookingRepository.getBookingByUserIdAndBookingStatusFuture(user.getId(), start.minusDays(1), Pageable.ofSize(4));

        assertThat(bookingJpaList.get(0), booking);
    }

    @Test
    void getBookingByUserIdAndBookingStatusWaitingTest() {
        List<Booking> bookingJpaList = bookingRepository.getBookingByUserIdAndBookingStatusWaiting(user.getId(), BookingStatus.WAITING, Pageable.ofSize(4));

        assertThat(bookingJpaList.get(0), booking);
    }

    @Test
    void getBookingByUserIdAndBookingStatusRejectedTest() {
        Booking bookingRejected = Booking.builder().id(1L).start(start.minusDays(1)).end(end.minusDays(1)).item(item).booker(user).bookingStatus(BookingStatus.REJECTED).build();
        bookingRepository.save(bookingRejected);

        List<Booking> bookingJpaList = bookingRepository.getBookingByUserIdAndBookingStatusRejected(user.getId(), BookingStatus.REJECTED, Pageable.ofSize(4));

        assertThat(bookingJpaList.get(0), bookingRejected);
    }

    @Test
    void getAllBookingByOwnerIdTest() {
        List<Booking> bookingJpaList = bookingRepository.getAllBookingByOwnerId(owner.getId(), Pageable.ofSize(4));

        assertThat(bookingJpaList.get(0), booking);
    }

    @Test
    void getBookingByOwnerIdAndBookingStatusCurrentTest() {
        List<Booking> bookingJpaList = bookingRepository.getBookingByOwnerIdAndBookingStatusCurrent(owner.getId(), start.plusSeconds(5), Pageable.ofSize(4));

        assertThat(bookingJpaList.get(0), booking);
    }

    @Test
    void getBookingByOwnerIdAndBookingStatusPastTest() {
        List<Booking> bookingJpaList = bookingRepository.getBookingByOwnerIdAndBookingStatusPast(owner.getId(), start.plusDays(1), Pageable.ofSize(4));

        assertThat(bookingJpaList.get(0), booking);
    }

    @Test
    void getBookingByOwnerIdAndBookingStatusFutureTest() {
        List<Booking> bookingJpaList = bookingRepository.getBookingByOwnerIdAndBookingStatusFuture(owner.getId(), start.minusDays(1), Pageable.ofSize(4));

        assertThat(bookingJpaList.get(0), booking);
    }

    @Test
    void getBookingByOwnerIdAndBookingStatusWaitingTest() {
        List<Booking> bookingJpaList = bookingRepository.getBookingByOwnerIdAndBookingStatusWaiting(owner.getId(), BookingStatus.WAITING, Pageable.ofSize(4));

        assertThat(bookingJpaList.get(0), booking);
    }

    @Test
    void getBookingByOwnerIdAndBookingStatusRejectedTest() {
        Booking bookingRejected = Booking.builder().id(1L).start(start.minusDays(1)).end(end.minusDays(1)).item(item).booker(user).bookingStatus(BookingStatus.REJECTED).build();
        bookingRepository.save(bookingRejected);

        List<Booking> bookingJpaList = bookingRepository.getBookingByOwnerIdAndBookingStatusRejected(owner.getId(), BookingStatus.REJECTED, Pageable.ofSize(4));

        assertThat(bookingJpaList.get(0), bookingRejected);
    }

    @Test
    void findByItemIdLastTest() {
        Booking bookingApproved = Booking.builder().id(1L).start(start.minusDays(1)).end(end.minusDays(1)).item(item).booker(user).bookingStatus(BookingStatus.APPROVED).build();
        bookingRepository.save(bookingApproved);

        Booking bookingJpa = bookingRepository.findByItemIdLast(owner.getId(), item.getId(), start.plusDays(2));

        assertThat(bookingJpa, bookingApproved);
    }

    @Test
    void findByItemIdNextTest() {
        Booking bookingApproved = Booking.builder().id(1L).start(start.minusDays(1)).end(end.minusDays(1)).item(item).booker(user).bookingStatus(BookingStatus.APPROVED).build();
        bookingRepository.save(bookingApproved);

        Booking bookingJpa = bookingRepository.findByItemIdNext(owner.getId(), item.getId(), start.minusDays(2));

        assertThat(bookingJpa, bookingApproved);
    }

    @Test
    void getBookingByBookerIdAndItemIdTest() {
        Booking bookingApproved = Booking.builder().id(1L).start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.APPROVED).build();
        bookingRepository.save(bookingApproved);

        Booking bookingJpa = bookingRepository.getBookingByBookerIdAndItemId(user.getId(), item.getId(), start.plusDays(2));

        assertThat(bookingJpa, booking);
    }
}
