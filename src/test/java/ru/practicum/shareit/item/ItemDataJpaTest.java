package ru.practicum.shareit.item;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
public class ItemDataJpaTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    User owner = User.builder().name("Owner").email("owner@ayndex.ru").build();
    Item item = Item.builder().owner(owner).name("Item").description("Item").available(true).request(null).build();

    @BeforeEach
    void dependencyInject() {
        userRepository.save(owner);
        itemRepository.save(item);
    }

    @AfterEach
    void clearInject() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void searchAvailableItemTest() {
        List<Item> itemList = itemRepository.searchAvailableItem("Item", Pageable.ofSize(4));

        MatcherAssert.assertThat(itemList.get(0).getId(), notNullValue());
        MatcherAssert.assertThat(itemList.get(0).getDescription(), equalTo(item.getDescription()));
        MatcherAssert.assertThat(itemList.get(0).getName(), equalTo(item.getName()));
        MatcherAssert.assertThat(itemList.get(0).getRequest(), equalTo(item.getRequest()));
        MatcherAssert.assertThat(itemList.get(0).getOwner(), equalTo(item.getOwner()));
    }
}
