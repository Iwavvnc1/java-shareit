package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@RequiredArgsConstructor
@Component
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public User getById(long userId) {
        return userStorage.getById(userId);
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }

    @Override
    public User update(long userId, UserDto user) {
        return userStorage.update(user, userId);
    }

    @Override
    public void delete(long userId) {
        userStorage.delete(userId);
    }
}
