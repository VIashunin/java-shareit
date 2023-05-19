package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.EmailIsAlreadyExistsException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserMapper userMapper;
    private final List<User> userList = new ArrayList<>();
    private long id = 1;

    @Override
    public List<UserDto> getAllUsers() {
        return userMapper.mapFromUserListToUserDtoList(userList);
    }

    @Override
    public UserDto getUserById(long userId) {
        return userMapper.mapFromUserToUserDto(userList.stream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(userId)));
    }

    @Override
    public UserDto addUser(User user) {
        if (validate(user)) {
            user.setId(id++);
            userList.add(user);
        }
        return userMapper.mapFromUserToUserDto(user);
    }

    @Override
    public UserDto updateUser(long userId, User user) {
        user.setId(userId);
        validate(user);
        for (User u : userList) {
            if (u.getId() == userId) {
                if (user.getName() != null) {
                    u.setName(user.getName());
                }
                if (user.getEmail() != null) {
                    u.setEmail(user.getEmail());
                }
            }
        }
        return getUserById(userId);
    }

    @Override
    public void deleteUser(long userId) {
        userList.removeAll(userList.stream()
                .filter(user -> user.getId() == userId)
                .collect(Collectors.toList()));
    }

    private boolean validate(User otherUser) {
        if ((userList.stream()
                .noneMatch(user -> user.getEmail().equals(otherUser.getEmail())))
                || (userList.stream()
                .noneMatch(user -> user.getEmail().equals(otherUser.getEmail())
                        && user.getId() != otherUser.getId()))) {
            return true;
        } else {
            throw new EmailIsAlreadyExistsException(otherUser.getEmail());
        }
    }
}
