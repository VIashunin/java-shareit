package ru.practicum.shareit.request;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
public class ItemRequestDataJpaTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;

    LocalDateTime start = LocalDateTime.of(2025, 5, 25, 15, 15, 13);

    User ownerFirst = User.builder().name("Owner1").email("owner1@yandex.ru").build();
    User ownerSecond = User.builder().name("Owner2").email("owner2@yandex.ru").build();
    ItemRequest itemRequestFirst = ItemRequest.builder().id(1L).description("Test").created(start).requestor(ownerFirst).build();
    ItemRequest itemRequestSecond = ItemRequest.builder().id(1L).description("Test").created(start).requestor(ownerSecond).build();

    @BeforeEach
    void dependencyInject() {
        userRepository.save(ownerFirst);
        userRepository.save(ownerSecond);
        itemRequestRepository.save(itemRequestFirst);
        itemRequestRepository.save(itemRequestSecond);
    }

    @AfterEach
    void clearInject() {
        userRepository.deleteAll();
    }

    @Test
    void findItemRequestByRequestorIdTest() {
        List<ItemRequest> itemRequestList = itemRequestRepository.findItemRequestByRequestor_Id(ownerFirst.getId());

        MatcherAssert.assertThat(itemRequestList.get(0).getId(), notNullValue());
        MatcherAssert.assertThat(itemRequestList.get(0).getDescription(), equalTo(itemRequestFirst.getDescription()));
        MatcherAssert.assertThat(itemRequestList.get(0).getCreated(), equalTo(itemRequestFirst.getCreated()));
        MatcherAssert.assertThat(itemRequestList.get(0).getRequestor(), equalTo(itemRequestFirst.getRequestor()));
    }

    @Test
    void findItemRequestByIdNotOrderByCreatedDescTest() {
        List<ItemRequest> itemRequestList = itemRequestRepository.findItemRequestByIdNotOrderByCreatedDesc(ownerSecond.getId(), Pageable.ofSize(4));

        MatcherAssert.assertThat(itemRequestList.get(0).getId(), notNullValue());
        MatcherAssert.assertThat(itemRequestList.get(0).getDescription(), equalTo(itemRequestSecond.getDescription()));
        MatcherAssert.assertThat(itemRequestList.get(0).getCreated(), equalTo(itemRequestSecond.getCreated()));
        MatcherAssert.assertThat(itemRequestList.get(0).getRequestor(), equalTo(itemRequestSecond.getRequestor()));
    }
}
