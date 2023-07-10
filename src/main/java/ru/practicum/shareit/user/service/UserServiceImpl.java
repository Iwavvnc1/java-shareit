package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.dto.UserMapper.*;

@RequiredArgsConstructor
@Component
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto getById(long userId) {
        return toUserDto(userRepository.findById(userId).get());
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto create(User user) {
        return toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto update(long userId, User user) {
        user.setId(userId);
        User saveUser = userRepository.findById(userId).get();
        if (user.getName() == null) {
            user.setName(saveUser.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(saveUser.getEmail());
        }
        return toUserDto(userRepository.save(user));
    }

    @Override
    public void delete(long userId) {
        userRepository.deleteById(userId);
    }
}
