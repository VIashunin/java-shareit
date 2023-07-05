package ru.practicum.server.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.server.exceptions.UserNotFoundException;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;
import ru.practicum.server.user.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

public class UserServiceImplTest {
    static UserServiceImpl userService = new UserServiceImpl();
    static UserRepository userRepository = Mockito.mock(UserRepository.class);

    static User user;
    static List<User> userList;
    static UserDto userDto;

    @BeforeAll
    static void beforeAll() {
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);

        user = User.builder().id(1L).name("User").email("user@yandex.ru").build();
        userList = List.of(user);
        userDto = UserDto.builder().id(1L).name("User").email("user@yandex.ru").build();
    }


    //normal behavior
    @Test
    void addUserTest() {
        Mockito.when(userRepository.save(user))
                .thenReturn(user);

        assertEquals(userDto, userService.addUser(userDto));
    }

    @Test
    void updateUserTest() {
        Optional<User> userOpt = Optional.of(User.builder().id(1L).name("User").email("user@yandex.ru").build());
        User userUpdate = User.builder().id(1L).name("Update").email("Update@yandex.ru").build();
        UserDto userDtoUpdate = UserDto.builder().id(1L).name("Update").email("Update@yandex.ru").build();
        Mockito.when(userRepository.findById(anyLong()))
                .thenReturn(userOpt);
        Mockito.when(userRepository.save(userUpdate))
                .thenReturn(userUpdate);

        assertEquals(userDtoUpdate, userService.updateUser(1L, userDtoUpdate));
    }

    @Test
    void findUserByIdTest() {
        Optional<User> userOpt = Optional.of(User.builder().id(1L).name("User").email("user@yandex.ru").build());
        Mockito.when(userRepository.findById(any()))
                .thenReturn(userOpt);

        assertEquals(user, userService.findUserById(1L));
    }

    @Test
    void getUserByIdTest() {
        Optional<User> userOpt = Optional.of(User.builder().id(1L).name("User").email("user@yandex.ru").build());
        Mockito.when(userRepository.findById(any()))
                .thenReturn(userOpt);

        assertEquals(userDto, userService.getUserById(1L));
    }

    @Test
    void getAllUsersTest() {
        Mockito.when(userRepository.findAll())
                .thenReturn(userList);

        assertEquals(userDto, userService.getAllUsers().get(0));
    }

    //Reaction to wrong data
    @Test
    void findUserByIdExceptionTest() {
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.findUserById(10L));

        assertEquals(exception.getMessage(), "User with id: 10 does not exist.");
    }

    @Test
    void getUserByIdExceptionTest() {
        Mockito.when(userRepository.findById(anyLong()))
                .thenThrow(UserNotFoundException.class);
        final UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserById(10L));

        assertNull(exception.getMessage());
    }
}
