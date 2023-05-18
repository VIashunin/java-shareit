package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public UserDto getUserById(long userId) {
        return userRepository.getUserById(userId);
    }

    @Override
    public UserDto addUser(User user) {
        return userRepository.addUser(user);
    }

    @Override
    public UserDto updateUser(long userId, User user) {
        return userRepository.updateUser(userId, user);
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteUser(userId);
    }
}