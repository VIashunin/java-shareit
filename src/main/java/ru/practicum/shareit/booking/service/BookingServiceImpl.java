package ru.practicum.shareit.booking.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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

import static ru.practicum.shareit.request.service.ItemRequestServiceImpl.getPageable;

@Service
@Transactional
@NoArgsConstructor
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;

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
            validationOfBookingDto(bookingDto);
            return BookingMapper.mapFromBookingToBookingDto(bookingRepository.save(BookingMapper.mapFromBookingDtoToBooking(bookingDto, item, user)));
        } else {
            throw new BookingOfOwnItemException();
        }
    }

    public void validationOfBookingDto(BookingDto bookingDto) {
        if (!bookingDto.getItem().isAvailable()) {
            throw new ItemNotAvailableException(bookingDto.getItem().getId());
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) || bookingDto.getEnd().isEqual(bookingDto.getStart())) {
            throw new EndBeforeOrEqualStartException();
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
                return BookingMapper.mapFromBookingToBookingDto(bookingRepository.save(booking));
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
            return BookingMapper.mapFromBookingToBookingDto(bookingRepository.getBookingById(bookingId, userId));
        } else {
            throw new NotOwnerOrNotBookerException("You are not the owner or the booker of requested item for showing this booking.");
        }
    }

    public List<BookingDto> getBookingsListByUserId(long userId, String state, Integer from, Integer size) {
        userService.findUserById(userId);
        Pageable page = paginate(from, size);
        try {
            BookingState bookingState = BookingState.valueOf(state);
            switch (bookingState) {
                case ALL:
                    return BookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getAllBookingByUserId(userId, page));
                case CURRENT:
                    return BookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getBookingByUserIdAndBookingStatusCurrent(userId, LocalDateTime.now(), page));
                case PAST:
                    return BookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getBookingByUserIdAndBookingStatusPast(userId, LocalDateTime.now(), page));
                case FUTURE:
                    return BookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getBookingByUserIdAndBookingStatusFuture(userId, LocalDateTime.now(), page));
                case WAITING:
                    return BookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getBookingByUserIdAndBookingStatusWaiting(userId, BookingStatus.WAITING, page));
                case REJECTED:
                    return BookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getBookingByUserIdAndBookingStatusRejected(userId, BookingStatus.REJECTED, page));
                default:
                    return List.of();
            }
        } catch (IllegalArgumentException e) {
            throw new UnknownBookingStateException("Unknown state: " + state);
        }
    }

    public List<BookingDto> getBookingsListByOwnerId(long userId, String state, Integer from, Integer size) {
        userService.findUserById(userId);
        Pageable page = paginate(from, size);
        try {
            BookingState bookingState = BookingState.valueOf(state);
            switch (bookingState) {
                case ALL:
                    return BookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getAllBookingByOwnerId(userId, page));
                case CURRENT:
                    return BookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getBookingByOwnerIdAndBookingStatusCurrent(userId, LocalDateTime.now(), page));
                case PAST:
                    return BookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getBookingByOwnerIdAndBookingStatusPast(userId, LocalDateTime.now(), page));
                case FUTURE:
                    return BookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getBookingByOwnerIdAndBookingStatusFuture(userId, LocalDateTime.now(), page));
                case WAITING:
                    return BookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getBookingByOwnerIdAndBookingStatusWaiting(userId, BookingStatus.WAITING, page));
                case REJECTED:
                    return BookingMapper.mapFromBookingListToBookingDtoList(bookingRepository.getBookingByOwnerIdAndBookingStatusRejected(userId, BookingStatus.REJECTED, page));
                default:
                    return List.of();
            }
        } catch (IllegalArgumentException e) {
            throw new UnknownBookingStateException("Unknown state: " + state);
        }
    }

    public Pageable paginate(Integer from, Integer size) {
        return getPageable(from, size);
    }
}
