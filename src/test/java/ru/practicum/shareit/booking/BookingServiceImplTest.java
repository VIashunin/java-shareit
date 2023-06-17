package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

public class BookingServiceImplTest {
    static BookingServiceImpl bookingService = new BookingServiceImpl();
    static BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
    static UserService userService = Mockito.mock(UserService.class);
    static ItemService itemService = Mockito.mock(ItemService.class);

    static User user;
    static User owner;
    static Item item;

    static BookingDto bookingDto;
    static Booking booking;
    static LocalDateTime start;
    static LocalDateTime end;


    @BeforeAll
    static void beforeAll() {
        ReflectionTestUtils.setField(bookingService, "bookingRepository", bookingRepository);
        ReflectionTestUtils.setField(bookingService, "userService", userService);
        ReflectionTestUtils.setField(bookingService, "itemService", itemService);

        user = User.builder().id(1L).name("User").email("user@yandex.ru").build();
        owner = User.builder().id(2L).name("Owner").email("owner@yandex.ru").build();
        item = Item.builder().id(1L).owner(owner).name("Item").description("Item").available(true).request(null).build();

        start = LocalDateTime.of(2021, 5, 5, 14, 25, 11);
        end = LocalDateTime.of(2024, 5, 5, 14, 27, 11);

        bookingDto = BookingDto.builder().start(start).end(end).status(BookingStatus.WAITING).booker(user).item(item).build();
        booking = Booking.builder().start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build();
    }


    //normal behavior
    @Test
    public void addBookingTest() {
        BookingShort bookingShort = BookingShort.builder().start(start).end(end).itemId(1L).build();

        Mockito.when(bookingRepository.save(booking))
                .thenReturn(booking);
        Mockito.when(userService.findUserById(anyLong()))
                .thenReturn(user);
        Mockito.when(itemService.findItemById(anyLong()))
                .thenReturn(item);

        assertEquals(bookingDto, bookingService.addBooking(user.getId(), bookingShort));
    }

    @Test
    public void confirmBookingTest() {
        Item itemTwo = Item.builder().id(1L).owner(owner).name("Item").description("Item").available(true).request(null).build();

        Booking bookingApproved = Booking.builder().id(1L).start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.APPROVED).build();
        Booking bookingRejected = Booking.builder().id(2L).start(start).end(end).item(itemTwo).booker(user).bookingStatus(BookingStatus.REJECTED).build();

        Optional<Booking> bookingOptional = Optional.of(Booking.builder().id(1L).start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build());
        Optional<Booking> bookingOptionalTwo = Optional.of(Booking.builder().id(2L).start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build());

        BookingDto bookingDtoApproved = BookingDto.builder().id(1L).start(start).end(end).status(BookingStatus.APPROVED).booker(user).item(item).build();
        BookingDto bookingDtoRejected = BookingDto.builder().id(2L).start(start).end(end).status(BookingStatus.REJECTED).booker(user).item(item).build();


        Mockito.when(bookingRepository.findById(1L))
                .thenReturn(bookingOptional);
        Mockito.when(bookingRepository.findById(2L))
                .thenReturn(bookingOptionalTwo);

        Mockito.when(bookingRepository.save(bookingApproved))
                .thenReturn(bookingApproved);
        Mockito.when(bookingRepository.save(bookingRejected))
                .thenReturn(bookingRejected);

        assertEquals(bookingDtoApproved, bookingService.confirmBooking(owner.getId(), 1L, true));
        assertEquals(bookingDtoRejected, bookingService.confirmBooking(owner.getId(), 2L, false));
    }

    @Test
    void getBookingByIdTest() {
        Optional<Booking> bookingOpt = Optional.of(Booking.builder().id(1L).start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build());

        Mockito.when(bookingRepository.findById(1L))
                .thenReturn(bookingOpt);
        Mockito.when(bookingRepository.getBookingById(1L, 1L))
                .thenReturn(booking);

        assertEquals(bookingDto, bookingService.getBookingById(1L, 1L));
    }

    @Test
    void getBookingsListByUserIdTest() {
        List<BookingDto> bookingDtoList = List.of(BookingDto.builder().start(start).end(end).status(BookingStatus.WAITING).booker(user).item(item).build());
        List<Booking> bookingList = List.of(Booking.builder().start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build());

        Mockito.when(bookingRepository.getAllBookingByUserId(1L, Pageable.ofSize(4)))
                .thenReturn(bookingList);
        Mockito.when(bookingRepository.getBookingByUserIdAndBookingStatusCurrent(anyLong(), any(), any()))
                .thenReturn(bookingList);
        Mockito.when(bookingRepository.getBookingByUserIdAndBookingStatusPast(anyLong(), any(), any()))
                .thenReturn(bookingList);
        Mockito.when(bookingRepository.getBookingByUserIdAndBookingStatusFuture(anyLong(), any(), any()))
                .thenReturn(bookingList);
        Mockito.when(bookingRepository.getBookingByUserIdAndBookingStatusWaiting(1L, BookingStatus.WAITING, Pageable.ofSize(4)))
                .thenReturn(bookingList);
        Mockito.when(bookingRepository.getBookingByUserIdAndBookingStatusRejected(1L, BookingStatus.REJECTED, Pageable.ofSize(4)))
                .thenReturn(bookingList);

        assertEquals(bookingDtoList, bookingService.getBookingsListByUserId(1L, "ALL", 0, 4));
        assertEquals(bookingDtoList, bookingService.getBookingsListByUserId(1L, "CURRENT", 0, 4));
        assertEquals(bookingDtoList, bookingService.getBookingsListByUserId(1L, "PAST", 0, 4));
        assertEquals(bookingDtoList, bookingService.getBookingsListByUserId(1L, "FUTURE", 0, 4));
        assertEquals(bookingDtoList, bookingService.getBookingsListByUserId(1L, "WAITING", 0, 4));
        assertEquals(bookingDtoList, bookingService.getBookingsListByUserId(1L, "REJECTED", 0, 4));
    }

    @Test
    void getBookingsListByOwnerIdTest() {
        List<BookingDto> bookingDtoList = List.of(BookingDto.builder().start(start).end(end).status(BookingStatus.WAITING).booker(user).item(item).build());
        List<Booking> bookingList = List.of(Booking.builder().start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build());

        Mockito.when(bookingRepository.getAllBookingByOwnerId(1L, Pageable.ofSize(4)))
                .thenReturn(bookingList);
        Mockito.when(bookingRepository.getBookingByOwnerIdAndBookingStatusCurrent(anyLong(), any(), any()))
                .thenReturn(bookingList);
        Mockito.when(bookingRepository.getBookingByOwnerIdAndBookingStatusPast(anyLong(), any(), any()))
                .thenReturn(bookingList);
        Mockito.when(bookingRepository.getBookingByOwnerIdAndBookingStatusFuture(anyLong(), any(), any()))
                .thenReturn(bookingList);
        Mockito.when(bookingRepository.getBookingByOwnerIdAndBookingStatusWaiting(1L, BookingStatus.WAITING, Pageable.ofSize(4)))
                .thenReturn(bookingList);
        Mockito.when(bookingRepository.getBookingByOwnerIdAndBookingStatusRejected(1L, BookingStatus.REJECTED, Pageable.ofSize(4)))
                .thenReturn(bookingList);

        assertEquals(bookingDtoList, bookingService.getBookingsListByOwnerId(1L, "ALL", 0, 4));
        assertEquals(bookingDtoList, bookingService.getBookingsListByOwnerId(1L, "CURRENT", 0, 4));
        assertEquals(bookingDtoList, bookingService.getBookingsListByOwnerId(1L, "PAST", 0, 4));
        assertEquals(bookingDtoList, bookingService.getBookingsListByOwnerId(1L, "FUTURE", 0, 4));
        assertEquals(bookingDtoList, bookingService.getBookingsListByOwnerId(1L, "WAITING", 0, 4));
        assertEquals(bookingDtoList, bookingService.getBookingsListByOwnerId(1L, "REJECTED", 0, 4));
    }

    @Test
    void paginateTest() {
        Pageable page = PageRequest.of(0, 4);
        assertEquals(page, bookingService.paginate(0, 4));
    }

    //Reaction to wrong data
    @Test
    public void addBookingExceptionTest() {
        BookingShort bookingShort = BookingShort.builder().start(start).end(end).itemId(1L).build();

        Mockito.when(bookingRepository.save(booking))
                .thenReturn(booking);
        Mockito.when(userService.findUserById(anyLong()))
                .thenReturn(owner);
        Mockito.when(itemService.findItemById(anyLong()))
                .thenReturn(item);

        final BookingOfOwnItemException exception = assertThrows(BookingOfOwnItemException.class, () -> bookingService.addBooking(user.getId(), bookingShort));

        assertEquals(exception.getMessage(), "You can't book your own item.");
    }

    @Test
    public void confirmBookingExceptionTest() {
        Optional<Booking> bookingOpt = Optional.of(Booking.builder().id(1L).start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.APPROVED).build());

        Mockito.when(bookingRepository.findById(1L))
                .thenReturn(bookingOpt);

        final NotOwnerOrNotBookerException exceptionFirst = assertThrows(NotOwnerOrNotBookerException.class,
                () -> bookingService.confirmBooking(user.getId(), 1L, true));
        final BookingAlreadyApprovedException exceptionSecond = assertThrows(BookingAlreadyApprovedException.class,
                () -> bookingService.confirmBooking(owner.getId(), 1L, true));


        assertEquals(exceptionFirst.getMessage(), "You are not the owner of requested item for booking confirmation.");
        assertEquals(exceptionSecond.getMessage(), "Booking with id: 1 is already approved.");
    }

    @Test
    void getBookingByIdExceptionTest() {
        Optional<Booking> bookingOpt = Optional.of(Booking.builder().id(1L).start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build());

        Mockito.when(bookingRepository.findById(1L))
                .thenReturn(bookingOpt);
        Mockito.when(bookingRepository.getBookingById(1L, 1L))
                .thenReturn(booking);

        final NotOwnerOrNotBookerException exception = assertThrows(NotOwnerOrNotBookerException.class, () -> bookingService.getBookingById(3L, 1L));

        assertEquals(exception.getMessage(), "You are not the owner or the booker of requested item for showing this booking.");
    }

    @Test
    void getBookingsListByUserIdExceptionTest() {
        List<Booking> bookingList = List.of(Booking.builder().start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build());

        Mockito.when(bookingRepository.getAllBookingByUserId(1L, Pageable.ofSize(4)))
                .thenReturn(bookingList);

        final UnknownBookingStateException exception = assertThrows(UnknownBookingStateException.class,
                () -> bookingService.getBookingsListByUserId(1L, "STATE", 0, 4));

        assertEquals(exception.getMessage(), "Unknown state: STATE");
    }

    @Test
    void getBookingsListByOwnerIdExceptionTest() {
        List<Booking> bookingList = List.of(Booking.builder().start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build());

        Mockito.when(bookingRepository.getAllBookingByOwnerId(1L, Pageable.ofSize(4)))
                .thenReturn(bookingList);

        final UnknownBookingStateException exception = assertThrows(UnknownBookingStateException.class,
                () -> bookingService.getBookingsListByOwnerId(1L, "STATE", 0, 4));

        assertEquals(exception.getMessage(), "Unknown state: STATE");
    }

    @Test
    void paginateExceptionTest() {
        final PageableParametersAreInvalidException exception = assertThrows(PageableParametersAreInvalidException.class,
                () -> bookingService.paginate(-1, 4));

        assertEquals(exception.getMessage(), "From or size pageable parameters are invalid.");
    }

    @Test
    void validationOfBookingDtoExceptionTest() {
        Item itemException = Item.builder().id(1L).owner(owner).name("Item").description("Item").available(false).request(null).build();
        BookingDto bookingDtoExceptionFirst = BookingDto.builder().start(end).end(start).status(BookingStatus.WAITING).booker(user).item(item).build();
        BookingDto bookingDtoExceptionSecond = BookingDto.builder().start(start).end(end).status(BookingStatus.WAITING).booker(user).item(itemException).build();

        final EndBeforeOrEqualStartException exceptionFirst = assertThrows(EndBeforeOrEqualStartException.class,
                () -> bookingService.validationOfBookingDto(bookingDtoExceptionFirst));
        final ItemNotAvailableException exceptionSecond = assertThrows(ItemNotAvailableException.class,
                () -> bookingService.validationOfBookingDto(bookingDtoExceptionSecond));

        assertEquals(exceptionFirst.getMessage(), "End date and time can't be before start date and time.");
        assertEquals(exceptionSecond.getMessage(), "Item with id: 1 is not available.");
    }
}
