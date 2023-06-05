package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ValidationsException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Repository
public class UserStorageImpl implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    AtomicInteger idUser = new AtomicInteger(1);

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        User finalUser = user;
        if (users.values().stream().anyMatch(saveUser -> saveUser.getEmail().equals(finalUser.getEmail()))) {
            log.error("email = " + user.getEmail() + " is already in use.");
            throw new ValidationsException("Такой email уже используется");
        }
        user = user.toBuilder().id(idUser.longValue()).build();
        users.put(user.getId(), user);
        idUser.addAndGet(1);
        return user;
    }

    @Override
    public User update(UserDto userDto, Long userId) {
        if (users.values().stream().filter(saveUser -> saveUser.getEmail().equals(userDto.getEmail()))
                .anyMatch(saveUser -> !saveUser.getId().equals(userId))) {
            log.error("email = " + userDto.getEmail() + " is already in use");
            throw new ValidationsException("Такой email уже используется");
        }
        User updateUser = getById(userId);
        if (userDto.getName() != null) {
            updateUser = updateUser.toBuilder().name(userDto.getName()).build();
        }
        if (userDto.getEmail() != null) {
            updateUser = updateUser.toBuilder().email(userDto.getEmail()).build();
        }
        users.put(updateUser.getId(), updateUser);
        return updateUser;
    }

    @Override
    public void delete(Long userId) {
        users.remove(userId);
    }

    @Override
    public User getById(Long userId) {
        return users.get(userId);
    }
}
