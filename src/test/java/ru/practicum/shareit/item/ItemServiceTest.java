package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentShort;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = "spring.datasource.username=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    private final EntityManager em;
    private final ItemService itemService;
    private final BookingService bookingService;
    private final UserService userService;
    private final CommentService commentService;

    @Test
    void getAllItemsByUserIdTest() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusMinutes(5);
        User user = User.builder().id(1L).name("User").email("user@yandex.ru").build();
        User owner = User.builder().id(2L).name("Owner").email("owner@yandex.ru").build();
        UserDto userDto = UserDto.builder().id(1L).name("User").email("user@yandex.ru").build();
        UserDto userOwnerDto = UserDto.builder().id(2L).name("Owner").email("owner@yandex.ru").build();
        List<CommentDto> commentDtoList = List.of(CommentDto.builder().text("Text").authorName(user.getName()).created(end).build());
        Item item = Item.builder().id(1L).owner(owner).name("Item").description("Item").available(true).request(null).build();
        CommentShort commentShort = CommentShort.builder().text("Text").created(end).build();

        UserDto userDtoDb = userService.addUser(userDto);
        UserDto ownerDtoDb = userService.addUser(userOwnerDto);
        ItemDto itemDto = ItemDto.builder().name("Item").description("Item").available(true).owner(ownerDtoDb.getId()).requestId(null).build();
        List<ItemDtoWithBookingAndComments> itemDtoWithBookingAndCommentsList = List.of(ItemDtoWithBookingAndComments.builder()
                .id(1L).owner(ownerDtoDb.getId()).name(item.getName()).description("Item").available(true).comments(commentDtoList).build());
        ItemDto itemDtoDb = itemService.addItem(ownerDtoDb.getId(), itemDto);
        BookingShort bookingShort = BookingShort.builder().start(start).end(end).itemId(itemDtoDb.getId()).build();
        bookingService.addBooking(userDtoDb.getId(), bookingShort);
        commentService.addComment(userDtoDb.getId(), itemDtoDb.getId(), commentShort);

        TypedQuery<Item> query = em.createQuery("Select u from Item u where u.owner.id = :ownerId", Item.class);
        List<Item> itemList = query.setParameter("ownerId", ownerDtoDb.getId()).getResultList();

        assertThat(itemList.get(0).getId(), notNullValue());
        assertThat(itemList.get(0).getDescription(), equalTo(itemDtoWithBookingAndCommentsList.get(0).getDescription()));
        assertThat(itemList.get(0).getName(), equalTo(itemDtoWithBookingAndCommentsList.get(0).getName()));
        assertThat(itemList.get(0).getRequest(), equalTo(itemDtoWithBookingAndCommentsList.get(0).getRequest()));
        assertThat(itemList.get(0).getOwner().getId(), equalTo(itemDtoWithBookingAndCommentsList.get(0).getOwner()));
    }
}
