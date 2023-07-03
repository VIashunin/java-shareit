package ru.practicum.server.comment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.booking.repository.BookingRepository;
import ru.practicum.server.booking.status.BookingStatus;
import ru.practicum.server.comment.dto.CommentDto;
import ru.practicum.server.comment.dto.CommentShort;
import ru.practicum.server.comment.model.Comment;
import ru.practicum.server.comment.repository.CommentRepository;
import ru.practicum.server.comment.service.CommentServiceImpl;
import ru.practicum.server.exceptions.ItemWasNotBookedEarlierException;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

public class CommentServiceImplTest {
    static CommentServiceImpl commentService = new CommentServiceImpl();
    static CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    static BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);

    static User user;
    static User owner;
    static Item item;
    static LocalDateTime start;
    static LocalDateTime end;
    static Booking booking;
    static CommentShort commentShort;
    static CommentDto commentDto;
    static List<Comment> commentList;
    static Comment comment;

    @BeforeAll
    static void beforeAll() {
        ReflectionTestUtils.setField(commentService, "bookingRepository", bookingRepository);
        ReflectionTestUtils.setField(commentService, "commentRepository", commentRepository);

        user = User.builder().id(1L).name("User").email("user@yandex.ru").build();
        owner = User.builder().id(2L).name("Owner").email("owner@yandex.ru").build();
        item = Item.builder().id(1L).owner(owner).name("Item").description("Item").available(true).request(null).build();

        start = LocalDateTime.of(2020, 7, 15, 21, 15, 21);
        end = LocalDateTime.of(2021, 7, 15, 21, 15, 21);

        booking = Booking.builder().start(start).end(end).item(item).booker(user).bookingStatus(BookingStatus.WAITING).build();
        commentShort = CommentShort.builder().text("Text").created(start).build();
        commentDto = CommentDto.builder().text("Text").authorName(user.getName()).created(start).build();
        commentList = List.of(Comment.builder().text("Text").item(item).author(user).created(start).build());
        comment = Comment.builder().text("Text").item(item).author(user).created(start).build();
    }

    //normal behavior
    @Test
    void addCommentTest() {
        Mockito.when(bookingRepository.getBookingByBookerIdAndItemId(anyLong(), anyLong(), any()))
                .thenReturn(booking);
        Mockito.when(commentRepository.save(comment))
                .thenReturn(comment);

        assertEquals(commentDto, commentService.addComment(1L, 1L, commentShort));
    }

    @Test
    void getCommentsByItemIdTest() {
        Mockito.when(commentRepository.getCommentsByItemId(anyLong()))
                .thenReturn(commentList);

        assertEquals(commentDto, commentService.getCommentsByItemId(1L).get(0));
    }

    //Reaction to erroneous data
    @Test
    void addCommentExceptionTest() {
        Mockito.when(bookingRepository.getBookingByBookerIdAndItemId(anyLong(), anyLong(), any()))
                .thenReturn(null);

        final ItemWasNotBookedEarlierException exception = assertThrows(ItemWasNotBookedEarlierException.class,
                () -> commentService.addComment(1L, 1L, commentShort));

        assertEquals(exception.getMessage(), "This item was not booked by you earlier.");
    }
}
