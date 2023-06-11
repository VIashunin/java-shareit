package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.BookingState;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserService userService;
    private final ItemService itemService;

    public BookingDto addBooking(long userId, BookingShort bookingShort) {
        User user = userService.findUserById(userId);
        Item item = itemService.findItemById(bookingShort.getItemId());
        if (!item.getOwner().equals(user)) {
            BookingDto bookingDto = BookingDto.builder()
                    .start(bookingShort.getStart())
                    .end(bookingShort.getEnd())
                    .booker(user)
                    .item(item)
                    .status(BookingStatus.WAITING)
                    .build();
            if (!bookingDto.getItem().isAvailable()) {
                throw new ItemNotAvailableException(bookingDto.getItem().getId());
            }
            if (bookingDto.getEnd().isBefore(bookingDto.getStart()) || bookingDto.getEnd().isEqual(bookingDto.getStart())) {
                throw new EndBeforeOrEqualStartException();
            }
            return bookingMapper.mapFromBookingToBookingDto(bookingRepository.save(bookingMapper.mapFromBookingDtoToBooking(bookingDto, item, user)));
        } else {
            throw new BookingOfOwnItemException();
        }
    }

    public BookingDto confirmBooking(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException(bookingId));
        if (booking.getItem().getOwner().getId() == userId) {
            if (booking.getBookingStatus().equals(BookingStatus.WAITING)) {
                if (approved) {
                    booking.setBookingStatus(BookingStatus.APPROVED);
                } else {
                    booking.setBookingStatus(BookingStatus.REJECTED);
                }
                return bookingMapper.mapFromBookingToBookingDto(bookingRepository.save(booking));
            } else {
                throw new BookingAlreadyApprovedException(booking.getId());
            }
        } else {
            throw new NotOwnerOrNotBookerException("You are not the owner of requested item for booking confirmation.");
        }
    }

    public BookingDto getBookingById(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException(bookingId));
        if ((booking.getItem().getOwner().getId() == userId) || (booking.getBooker().getId() == userId)) {
            return bookingMapper.mapFromBookingToBookingDto(bookingRepository.getBookingById(bookingId, userId));
        } else {
            throw new NotOwnerOrNotBookerException("You are not the owner or the booker of requested item for showing this booking.");
        }
    }

    public List<BookingDto> getBookingsListByUserId(long userId, String state) {
        userService.findUserById(userId);
        try {
            BookingState bookingState = BookingState.valueOf(state);
            switch (bookingState) {
                case ALL:
                    return bookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getAllBookingByUserId(userId));
                case CURRENT:
                    return bookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getBookingByUserIdAndBookingStatusCurrent(userId, LocalDateTime.now()));
                case PAST:
                    return bookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getBookingByUserIdAndBookingStatusPast(userId, LocalDateTime.now()));
                case FUTURE:
                    return bookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getBookingByUserIdAndBookingStatusFuture(userId, LocalDateTime.now()));
                case WAITING:
                    return bookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getBookingByUserIdAndBookingStatusWaiting(userId, BookingStatus.WAITING));
                case REJECTED:
                    return bookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getBookingByUserIdAndBookingStatusRejected(userId, BookingStatus.REJECTED));
                default:
                    return List.of();
            }
        } catch (IllegalArgumentException e) {
            throw new UnknownBookingStateException("Unknown state: " + state);
        }
    }

    public List<BookingDto> getBookingsListByOwnerId(long userId, String state) {
        userService.findUserById(userId);
        try {
            BookingState bookingState = BookingState.valueOf(state);
            switch (bookingState) {
                case ALL:
                    return bookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getAllBookingByOwnerId(userId));
                case CURRENT:
                    return bookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getBookingByOwnerIdAndBookingStatusCurrent(userId, LocalDateTime.now()));
                case PAST:
                    return bookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getBookingByOwnerIdAndBookingStatusPast(userId, LocalDateTime.now()));
                case FUTURE:
                    return bookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getBookingByOwnerIdAndBookingStatusFuture(userId, LocalDateTime.now()));
                case WAITING:
                    return bookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getBookingByOwnerIdAndBookingStatusWaiting(userId, BookingStatus.WAITING));
                case REJECTED:
                    return bookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getBookingByOwnerIdAndBookingStatusRejected(userId, BookingStatus.REJECTED));
                default:
                    return List.of();
            }
        } catch (IllegalArgumentException e) {
            throw new UnknownBookingStateException("Unknown state: " + state);
        }
    }
}
