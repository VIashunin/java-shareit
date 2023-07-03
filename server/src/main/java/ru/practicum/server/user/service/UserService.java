package ru.practicum.server.user.service;

import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(long userId);

    UserDto addUser(UserDto userDto);

    UserDto updateUser(long userId, UserDto userDto);

    void deleteUser(long userId);

    User findUserById(long userId);
}
