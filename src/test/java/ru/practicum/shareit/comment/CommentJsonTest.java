package ru.practicum.shareit.comment;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.comment.dto.CommentShort;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CommentJsonTest {
    @Autowired
    private JacksonTester<CommentShort> jsonCommentShort;

    @Test
    @SneakyThrows
    void testCommentShort() {
        LocalDateTime created = LocalDateTime.of(2024, 6, 16, 19, 04, 13);
        CommentShort commentShort = CommentShort.builder().text("Text").created(created).build();

        JsonContent<CommentShort> result = jsonCommentShort.write(commentShort);

        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Text");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(created.toString());
    }
}
