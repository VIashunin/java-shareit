package ru.practicum.server.user.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.server.exceptions.UserNotFoundException;
import ru.practicum.server.user.dto.UserDto;
import ru.practicum.server.user.mapper.UserMapper;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@NoArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return UserMapper.mapFromUserListToUserDtoList(userRepository.findAll());
    }

    @Override
    public UserDto getUserById(long userId) {
        return UserMapper.mapFromUserToUserDto(findUserById(userId));
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        return UserMapper.mapFromUserToUserDto(userRepository.save(UserMapper.mapFromUserDtoToUser(userDto)));
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        UserDto userDtoForUpdate = getUserById(userId);
        if (userDto.getName() != null) {
            userDtoForUpdate.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userDtoForUpdate.setEmail(userDto.getEmail());
        }
        return addUser(userDtoForUpdate);
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
