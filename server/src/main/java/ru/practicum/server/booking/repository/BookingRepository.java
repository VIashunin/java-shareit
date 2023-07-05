package ru.practicum.server.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.booking.status.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = "select b.* from bookings as b " +
            "left join items as i on b.item_id = i.id " +
            "where b.id = ?1 and (b.booker_id = ?2 or i.owner_id = ?2) " +
            "group by b.id", nativeQuery = true)
    Booking getBookingById(long bookingId, long userId);

    @Query(value = "select b " +
            "from Booking as b " +
            "join fetch b.item as i " +
            "join fetch b.booker as u " +
            "where b.booker.id = ?1 " +
            "order by b.start desc")
    List<Booking> getAllBookingByUserId(long userId, Pageable page);

    @Query(value = "select b " +
            "from Booking as b " +
            "join fetch b.item as i " +
            "join fetch b.booker as u " +
            "where b.booker.id = ?1 and ?2 between b.start and b.end " +
            "order by b.end desc")
    List<Booking> getBookingByUserIdAndBookingStatusCurrent(long userId, LocalDateTime time, Pageable page);

    @Query(value = "select b " +
            "from Booking as b " +
            "join fetch b.item as i " +
            "join fetch b.booker as u " +
            "where b.booker.id = ?1 and b.end < ?2 " +
            "order by b.end desc")
    List<Booking> getBookingByUserIdAndBookingStatusPast(long userId, LocalDateTime time, Pageable page);

    @Query(value = "select b " +
            "from Booking as b " +
            "join fetch b.item as i " +
            "join fetch b.booker as u " +
            "where b.booker.id = ?1 and b.start > ?2 " +
            "order by b.end desc")
    List<Booking> getBookingByUserIdAndBookingStatusFuture(long userId, LocalDateTime time, Pageable page);

    @Query(value = "select b " +
            "from Booking as b " +
            "join fetch b.item as i " +
            "join fetch b.booker as u " +
            "where b.booker.id = ?1 and b.bookingStatus like ?2 " +
            "order by b.end desc")
    List<Booking> getBookingByUserIdAndBookingStatusWaiting(long userId, BookingStatus bookingStatus, Pageable page);

    @Query(value = "select b " +
            "from Booking as b " +
            "join fetch b.item as i " +
            "join fetch b.booker as u " +
            "where b.booker.id = ?1 and b.bookingStatus like ?2 " +
            "order by b.end desc")
    List<Booking> getBookingByUserIdAndBookingStatusRejected(long userId, BookingStatus bookingStatus, Pageable page);

    @Query(value = "select b " +
            "from Booking as b " +
            "join fetch b.item as i " +
            "join fetch b.booker as u " +
            "where i.owner.id = ?1 " +
            "order by b.start desc")
    List<Booking> getAllBookingByOwnerId(long userId, Pageable page);

    @Query(value = "select b " +
            "from Booking as b " +
            "join fetch b.item as i " +
            "join fetch b.booker as u " +
            "where i.owner.id = ?1 and ?2 between b.start and b.end " +
            "order by b.end desc")
    List<Booking> getBookingByOwnerIdAndBookingStatusCurrent(long userId, LocalDateTime time, Pageable page);

    @Query(value = "select b " +
            "from Booking as b " +
            "join fetch b.item as i " +
            "join fetch b.booker as u " +
            "where i.owner.id = ?1 and b.end < ?2 " +
            "order by b.end desc")
    List<Booking> getBookingByOwnerIdAndBookingStatusPast(long userId, LocalDateTime time, Pageable page);

    @Query(value = "select b " +
            "from Booking as b " +
            "join fetch b.item as i " +
            "join fetch b.booker as u " +
            "where i.owner.id = ?1 and b.start > ?2 " +
            "order by b.end desc")
    List<Booking> getBookingByOwnerIdAndBookingStatusFuture(long userId, LocalDateTime time, Pageable page);

    @Query(value = "select b " +
            "from Booking as b " +
            "join fetch b.item as i " +
            "join fetch b.booker as u " +
            "where i.owner.id = ?1 and b.bookingStatus like ?2 " +
            "order by b.end desc")
    List<Booking> getBookingByOwnerIdAndBookingStatusWaiting(long userId, BookingStatus bookingStatus, Pageable page);

    @Query(value = "select b " +
            "from Booking as b " +
            "join fetch b.item as i " +
            "join fetch b.booker as u " +
            "where i.owner.id = ?1 and b.bookingStatus like ?2 " +
            "order by b.end desc")
    List<Booking> getBookingByOwnerIdAndBookingStatusRejected(long userId, BookingStatus bookingStatus, Pageable page);

    @Query(value = "select b.* from bookings as b " +
            "left join items as i on b.item_id = i.id " +
            "left join users as u on i.owner_id = u.id " +
            "where i.owner_id = ?1 and i.id = ?2 and b.booking_status like 'APPROVED' and b.start_date <= ?3 " +
            "order by b.start_date desc " +
            "limit 1", nativeQuery = true)
    Booking findByItemIdLast(long userId, long itemId, LocalDateTime time);

    @Query(value = "select b.* from bookings as b " +
            "left join items as i on b.item_id = i.id " +
            "left join users as u on i.owner_id = u.id " +
            "where i.owner_id = ?1 and i.id = ?2 and b.booking_status like 'APPROVED' and b.start_date > ?3 " +
            "order by b.start_date asc " +
            "limit 1", nativeQuery = true)
    Booking findByItemIdNext(long userId, long itemId, LocalDateTime time);

    @Query(value = "select b.* from bookings as b " +
            "left join items as i on b.item_id = i.id " +
            "left join users as u on i.owner_id = u.id " +
            "where b.booker_id = ?1 and i.id = ?2 and i.owner_id != ?1 and b.start_date < ?3 " +
            "order by b.end_date desc " +
            "limit 1", nativeQuery = true)
    Booking getBookingByBookerIdAndItemId(long userId, long itemId, LocalDateTime time);
}
