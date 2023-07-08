package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    UserDto getById(long userId);

    List<UserDto> getAll();

    UserDto create(User user);

    UserDto update(long userId, User user);

    void delete(long userId);

}
