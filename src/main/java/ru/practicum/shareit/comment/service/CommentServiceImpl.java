package ru.practicum.shareit.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentShort;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exceptions.ItemWasNotBookedEarlierException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto addComment(long userId, long itemId, CommentShort commentShort) {
        Booking booking = bookingRepository.getBookingByBookerIdAndItemId(userId, itemId, LocalDateTime.now());
        if ((booking != null) && !booking.getBookingStatus().equals(BookingStatus.REJECTED)) {
            CommentDto commentDto = CommentDto.builder()
                    .text(commentShort.getText())
                    .authorName(booking.getBooker().getName())
                    .created(commentShort.getCreated())
                    .build();
            if (commentDto.getCreated() == null) {
                commentDto.setCreated(LocalDateTime.now());
            }
            Comment comment = commentMapper.mapFromCommentDtoToComment(commentDto, booking.getBooker(), booking.getItem());
            return commentMapper.mapFromCommentToCommentDto(commentRepository.save(comment));
        } else {
            throw new ItemWasNotBookedEarlierException();
        }
    }

    @Override
    public List<CommentDto> getCommentsByItemId(long itemId) {
        return commentMapper.mapFromCommentListToCommentDtoList(commentRepository.getCommentsByItemId(itemId));
    }
}
