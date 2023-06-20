package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    @Test
    @SneakyThrows
    void addRequestTest() {
        LocalDateTime created = LocalDateTime.of(2024, 6, 15, 21, 15, 31);
        User user = User.builder().id(1L).name("User").email("user@yandex.ru").build();
        List<ItemDto> itemDtoList = List.of(ItemDto.builder().id(1L).name("Item").description("Item").available(true).owner(user.getId()).requestId(null).build());
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1L).description("Test").created(created).requestor(user.getId()).items(itemDtoList).build();

        when(itemRequestService.addRequest(anyLong(), any()))
                .thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.requestor", is(itemRequestDto.getRequestor()), Long.class))
                .andExpect(jsonPath("$.items", notNullValue()));
    }

    @Test
    @SneakyThrows
    void getRequestTest() {
        LocalDateTime created = LocalDateTime.of(2024, 6, 15, 21, 15, 31);
        User user = User.builder().id(1L).name("User").email("user@yandex.ru").build();
        List<ItemDto> itemDtoList = List.of(ItemDto.builder().id(1L).name("Item").description("Item").available(true).owner(user.getId()).requestId(null).build());
        List<ItemRequestDto> itemRequestDtoList = List.of(ItemRequestDto.builder().id(1L).description("Test").created(created).requestor(user.getId()).items(itemDtoList).build());

        when(itemRequestService.getRequest(anyLong()))
                .thenReturn(itemRequestDtoList);

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDtoList)));
    }

    @Test
    @SneakyThrows
    void getAllRequestsTest() {
        LocalDateTime created = LocalDateTime.of(2024, 6, 15, 21, 15, 31);
        User user = User.builder().id(1L).name("User").email("user@yandex.ru").build();
        List<ItemDto> itemDtoList = List.of(ItemDto.builder().id(1L).name("Item").description("Item").available(true).owner(user.getId()).requestId(null).build());
        List<ItemRequestDto> itemRequestDtoList = List.of(ItemRequestDto.builder().id(1L).description("Test").created(created).requestor(user.getId()).items(itemDtoList).build());

        when(itemRequestService.getAllRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(itemRequestDtoList);

        mvc.perform(get("/requests/all?from=0&size=4")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequestDtoList)));
    }

    @Test
    @SneakyThrows
    void getRequestByIdTest() {
        LocalDateTime created = LocalDateTime.of(2024, 6, 15, 21, 15, 31);
        User user = User.builder().id(1L).name("User").email("user@yandex.ru").build();
        List<ItemDto> itemDtoList = List.of(ItemDto.builder().id(1L).name("Item").description("Item").available(true).owner(user.getId()).requestId(null).build());
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1L).description("Test").created(created).requestor(user.getId()).items(itemDtoList).build();

        when(itemRequestService.getRequestById(anyLong(), anyLong()))
                .thenReturn(itemRequestDto);

        mvc.perform(get("/requests/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.requestor", is(itemRequestDto.getRequestor()), Long.class))
                .andExpect(jsonPath("$.items", notNullValue()));
    }
}