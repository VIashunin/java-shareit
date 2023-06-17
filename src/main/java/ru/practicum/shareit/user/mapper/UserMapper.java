package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static UserDto mapFromUserToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static List<UserDto> mapFromUserListToUserDtoList(List<User> listUser) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : listUser) {
            userDtoList.add(UserDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .build());
        }
        return userDtoList;
    }

    public static User mapFromUserDtoToUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}
