package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    List<UserDto> getAllUsers();

    UserDto getUserById(long userId);

    UserDto addUser(User user);

    UserDto updateUser(long userId, User user);

    void deleteUser(long userId);
}
