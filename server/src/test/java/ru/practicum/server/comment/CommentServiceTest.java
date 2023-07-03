package ru.practicum.server.comment;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.server.booking.dto.BookingShort;
import ru.practicum.server.booking.service.BookingService;
import ru.practicum.server.comment.dto.CommentDto;
import ru.practicum.server.comment.dto.CommentShort;
import ru.practicum.server.comment.model.Comment;
import ru.practicum.server.comment.service.CommentService;
import ru.practicum.server.item.dto.ItemDto;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.service.ItemService;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.service.UserService;

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
public class CommentServiceTest {
    private final EntityManager em;
    private final CommentService commentService;
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    @Test
    void addCommentTest() {
        User owner = User.builder().id(2L).name("Owner").email("owner@yandex.ru").build();
        UserDto userDto = UserDto.builder().id(1L).name("User").email("user@yandex.ru").build();
        UserDto userOwnerDto = UserDto.builder().id(2L).name("Owner").email("owner@yandex.ru").build();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusMinutes(1);
        Item item = Item.builder().id(1L).owner(owner).name("Item").description("Item").available(true).request(null).build();
        ItemDto itemDto = ItemDto.builder().name("Item").description("Item").available(true).owner(owner.getId()).requestId(null).build();
        CommentShort commentShort = CommentShort.builder().text("Text").created(end).build();

        userService.addUser(userDto);
        UserDto ownerDtoDb = userService.addUser(userOwnerDto);
        ItemDto itemDtoDb = itemService.addItem(userService.getAllUsers().get(0).getId(), itemDto);
        BookingShort bookingShort = BookingShort.builder().start(start).end(end).itemId(itemDtoDb.getId()).build();
        CommentDto commentDto = CommentDto.builder().text("Text").authorName(ownerDtoDb.getName()).created(end).build();
        bookingService.addBooking(userService.getAllUsers().get(1).getId(), bookingShort);
        commentService.addComment(userService.getAllUsers().get(1).getId(), itemDtoDb.getId(), commentShort);

        TypedQuery<Comment> query = em.createQuery("Select u from Comment u where u.author.id = :userId", Comment.class);
        Comment comment = query.setParameter("userId", ownerDtoDb.getId()).getSingleResult();

        assertThat(comment.getId(), notNullValue());
        assertThat(comment.getText(), equalTo(commentDto.getText()));
        assertThat(comment.getAuthor().getName(), equalTo(commentDto.getAuthorName()));
        assertThat(comment.getItem().getId(), equalTo(itemDtoDb.getId()));
        assertThat(comment.getCreated(), equalTo(commentDto.getCreated()));
    }
}
