package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.exceptions.PageableParametersAreInvalidException;
import ru.practicum.shareit.exceptions.RequestNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;

public class ItemRequestServiceImplTest {
    static ItemRequestServiceImpl itemRequestService = new ItemRequestServiceImpl();

    static ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    static ItemRequestRepository itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
    static UserService userService = Mockito.mock(UserService.class);

    @BeforeAll
    static void beforeAll() {
        ReflectionTestUtils.setField(itemRequestService, "itemRepository", itemRepository);
        ReflectionTestUtils.setField(itemRequestService, "itemRequestRepository", itemRequestRepository);
        ReflectionTestUtils.setField(itemRequestService, "userService", userService);
    }

    @Test
    void addRequestTest() {
        LocalDateTime start = LocalDateTime.now();
        User user = User.builder().id(1L).name("User").email("user@yandex.ru").build();
        ItemRequest itemRequest = ItemRequest.builder().id(1L).description("Test").created(start).requestor(user).build();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1L).description("Test").created(start).requestor(user.getId()).items(null).build();

        Mockito.when(userService.findUserById(anyLong()))
                .thenReturn(user);
        Mockito.when(itemRequestRepository.save(itemRequest))
                .thenReturn(itemRequest);

        assertEquals(itemRequestDto, itemRequestService.addRequest(1L, itemRequestDto));
    }

    @Test
    void getAllRequestsTest() {
        LocalDateTime start = LocalDateTime.now();
        User user = User.builder().id(1L).name("User").email("user@yandex.ru").build();
        List<Item> itemList = List.of(Item.builder().id(1L).owner(user).name("Item").description("Item").available(true).request(null).build());
        List<ItemDto> itemDtoList = List.of(ItemDto.builder().id(1L).name("Item").description("Item").available(true).owner(user.getId()).requestId(null).build());
        List<ItemRequest> itemRequestList = List.of(ItemRequest.builder().id(1L).description("Test").created(start).requestor(user).build());
        List<ItemRequestDto> itemRequestDtoList = List.of(ItemRequestDto.builder().id(1L).description("Test").created(start).requestor(user.getId()).items(itemDtoList).build());

        Mockito.when(userService.findUserById(anyLong()))
                .thenReturn(user);
        Mockito.when(itemRequestRepository.findItemRequestByIdNotOrderByCreatedDesc(1L, Pageable.ofSize(4)))
                .thenReturn(itemRequestList);
        Mockito.when(itemRepository.findItemByRequest_Id(anyLong()))
                .thenReturn(itemList);

        assertEquals(itemRequestDtoList, itemRequestService.getAllRequests(1L, 0, 4));
    }

    @Test
    void getRequestTest() {
        LocalDateTime start = LocalDateTime.now();
        User user = User.builder().id(1L).name("User").email("user@yandex.ru").build();
        List<Item> itemList = List.of(Item.builder().id(1L).owner(user).name("Item").description("Item").available(true).request(null).build());
        List<ItemDto> itemDtoList = List.of(ItemDto.builder().id(1L).name("Item").description("Item").available(true).owner(user.getId()).requestId(null).build());
        List<ItemRequest> itemRequestList = List.of(ItemRequest.builder().id(1L).description("Test").created(start).requestor(user).build());
        List<ItemRequestDto> itemRequestDtoList = List.of(ItemRequestDto.builder().id(1L).description("Test").created(start).requestor(user.getId()).items(itemDtoList).build());

        Mockito.when(userService.findUserById(anyLong()))
                .thenReturn(user);
        Mockito.when(itemRequestRepository.findItemRequestByRequestor_Id(1L))
                .thenReturn(itemRequestList);
        Mockito.when(itemRepository.findItemByRequest_Id(anyLong()))
                .thenReturn(itemList);

        assertEquals(itemRequestDtoList, itemRequestService.getRequest(1L));
    }

    //Reaction to wrong data
    @Test
    void getAllRequestsExceptionTest() {
        LocalDateTime start = LocalDateTime.now();
        User user = User.builder().id(1L).name("User").email("user@yandex.ru").build();
        List<Item> itemList = List.of(Item.builder().id(1L).owner(user).name("Item").description("Item").available(true).request(null).build());
        List<ItemRequest> itemRequestList = List.of(ItemRequest.builder().id(1L).description("Test").created(start).requestor(user).build());

        Mockito.when(userService.findUserById(anyLong()))
                .thenReturn(user);
        Mockito.when(itemRequestRepository.findItemRequestByIdNotOrderByCreatedDesc(1L, Pageable.ofSize(4)))
                .thenReturn(itemRequestList);
        Mockito.when(itemRepository.findItemByRequest_Id(anyLong()))
                .thenReturn(itemList);

        final PageableParametersAreInvalidException exception = assertThrows(PageableParametersAreInvalidException.class,
                () -> itemRequestService.getAllRequests(1L, -1, 4));

        assertEquals(exception.getMessage(), "From or size pageable parameters are invalid.");
    }

    @Test
    void addRequestExceptionTest() {
        LocalDateTime start = LocalDateTime.now();
        User user = User.builder().id(1L).name("User").email("user@yandex.ru").build();
        ItemRequest itemRequest = ItemRequest.builder().id(1L).description("Test").created(start).requestor(user).build();

        Mockito.when(itemRequestRepository.save(itemRequest))
                .thenReturn(itemRequest);

        final RequestNotFoundException exception = assertThrows(RequestNotFoundException.class, () -> itemRequestService.getRequestById(1L, 10L));

        assertEquals(exception.getMessage(), "Request with id: 10 was not found.");
    }
}
