package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.Validations;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserStorageImpl implements UserStorage {
    Map<Long, User> users = new HashMap<>();
    public static Long idUser = 1L;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        User finalUser = user;
        if (users.values().stream().anyMatch(saveUser -> saveUser.getEmail().equals(finalUser.getEmail()))) {
            throw new Validations("Такой email уже используется");
        }
        user = user.toBuilder().id(idUser).build();
        users.put(user.getId(), user);
        idUser++;
        return user;
    }

    @Override
    public User update(UserDto userDto, Long userId) {
        if (users.values().stream().filter(saveUser -> saveUser.getEmail().equals(userDto.getEmail()))
                .anyMatch(saveUser -> !saveUser.getId().equals(userId))) {
            throw new Validations("Такой email уже используется");
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
