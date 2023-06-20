package ru.practicum.shareit.request;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemRequestJsonTest {
    @Autowired
    private JacksonTester<ItemRequestDto> jsonItemRequestDto;

    @Test
    @SneakyThrows
    void testItemRequestDto() {
        LocalDateTime created = LocalDateTime.of(2024, 6, 16, 19, 04, 13);
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1L).description("Test").created(created).requestor(1L).items(List.of()).build();

        JsonContent<ItemRequestDto> result = jsonItemRequestDto.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Test");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(created.toString());
        assertThat(result).extractingJsonPathNumberValue("$.requestor").isEqualTo(1);
        assertThat(result).extractingJsonPathArrayValue("$.items").isEqualTo(List.of());
    }
}
