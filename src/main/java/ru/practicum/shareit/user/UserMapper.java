package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toUserDto(User user) {
        return new UserDto()
                .toBuilder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail()).build();
    }

    public User toUser(UserDto userDto) {
        return new User()
                .toBuilder().id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail()).build();
    }
}
