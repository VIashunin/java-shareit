package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
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
public class ItemRequestServiceTest {
    private final EntityManager em;
    private final ItemRequestService itemRequestService;
    private final UserService userService;

    @Test
    void getAllRequestsTest() {
        User user = User.builder().id(1L).name("User").email("user@yandex.ru").build();
        UserDto userDto = UserDto.builder().id(1L).name("User").email("user@yandex.ru").build();
        LocalDateTime start = LocalDateTime.now();
        List<ItemDto> itemDtoList = List.of(ItemDto.builder().id(1L).name("Item").description("Item").available(true).owner(user.getId()).requestId(null).build());

        UserDto userDtoDb = userService.addUser(userDto);
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1L).description("Test").created(start).requestor(userDtoDb.getId()).items(itemDtoList).build();
        itemRequestService.addRequest(userDtoDb.getId(), itemRequestDto);

        TypedQuery<ItemRequest> query = em.createQuery("Select u from ItemRequest u where u.requestor.id = :userId", ItemRequest.class);
        ItemRequest itemRequest = query.setParameter("userId", userDtoDb.getId()).getSingleResult();

        assertThat(itemRequest.getId(), notNullValue());
        assertThat(itemRequest.getDescription(), equalTo(itemRequestDto.getDescription()));
        assertThat(itemRequest.getCreated(), equalTo(itemRequestDto.getCreated()));
        assertThat(itemRequest.getRequestor().getId(), equalTo(itemRequestDto.getRequestor()));
    }
}
