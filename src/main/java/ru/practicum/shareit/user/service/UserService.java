package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User getById(long userId);

    List<User> getAll();

    User create(User user);

    User update(long userId, UserDto user);

    void delete(long userId);

}
