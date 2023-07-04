package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto get(@PathVariable("id") Long userId) {
        log.info("Get user with id = " + userId);
        return UserMapper.toUserDto(userService.getById(userId));
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Get all user");
        return userService.getAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody User user) {
        log.info("Create new user");
        return UserMapper.toUserDto(userService.create(user));
    }

    @PatchMapping("/{id}")
    public UserDto update(@Valid @PathVariable("id") Long userId, @RequestBody UserDto user) {
        log.info("Update user with id = " + userId);
        return UserMapper.toUserDto(userService.update(userId, user));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long userId) {
        log.info("Delete user with id = " + userId);
        userService.delete(userId);
    }

}
