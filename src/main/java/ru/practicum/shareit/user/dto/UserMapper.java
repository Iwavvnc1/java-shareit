package ru.practicum.shareit.user.dto;

import lombok.Value;
import ru.practicum.shareit.user.model.User;

@Value
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
