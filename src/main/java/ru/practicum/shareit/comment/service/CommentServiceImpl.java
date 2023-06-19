package ru.practicum.shareit.comment.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@NoArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public CommentDto addComment(long userId, long itemId, CommentShort commentShort) {
        LocalDateTime created = LocalDateTime.now();
        if (commentShort.getCreated() == null) {
            commentShort.setCreated(created);
        }
        Booking booking = bookingRepository.getBookingByBookerIdAndItemId(userId, itemId, created);
        if ((booking != null) && !booking.getBookingStatus().equals(BookingStatus.REJECTED)) {
            CommentDto commentDto = CommentDto.builder()
                    .text(commentShort.getText())
                    .authorName(booking.getBooker().getName())
                    .created(commentShort.getCreated())
                    .build();
            Comment comment = CommentMapper.mapFromCommentDtoToComment(commentDto, booking.getBooker(), booking.getItem());
            return CommentMapper.mapFromCommentToCommentDto(commentRepository.save(comment));
        } else {
            throw new ItemWasNotBookedEarlierException();
        }
    }

    @Override
    public List<CommentDto> getCommentsByItemId(long itemId) {
        return CommentMapper.mapFromCommentListToCommentDtoList(commentRepository.getCommentsByItemId(itemId));
    }
}
