package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getById(long userId);

    List<UserDto> getAll();

    UserDto create(UserDto user);

    UserDto update(long userId, UserDto user);

    void delete(long userId);

}
