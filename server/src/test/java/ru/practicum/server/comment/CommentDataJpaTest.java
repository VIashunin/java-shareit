package ru.practicum.server.comment;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.server.comment.model.Comment;
import ru.practicum.server.comment.repository.CommentRepository;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.repository.ItemRepository;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
public class CommentDataJpaTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    LocalDateTime start = LocalDateTime.now();
    User owner = User.builder().name("Owner").email("owner@yandex.ru").build();
    Item item = Item.builder().owner(owner).name("Item").description("Item").available(true).request(null).build();
    Comment comment = Comment.builder().id(1L).text("text").item(item).author(owner).created(start).build();

    @BeforeEach
    void dependencyInject() {
        userRepository.save(owner);
        itemRepository.save(item);
        commentRepository.save(comment);
    }

    @AfterEach
    void clearInject() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    void getCommentsByItemIdTest() {
        List<Comment> commentList = commentRepository.getCommentsByItemId(item.getId());

        MatcherAssert.assertThat(commentList.get(0).getId(), notNullValue());
        MatcherAssert.assertThat(commentList.get(0).getText(), equalTo(comment.getText()));
        MatcherAssert.assertThat(commentList.get(0).getItem(), equalTo(comment.getItem()));
        MatcherAssert.assertThat(commentList.get(0).getAuthor(), equalTo(comment.getAuthor()));
        MatcherAssert.assertThat(commentList.get(0).getCreated(), equalTo(comment.getCreated()));
    }
}
