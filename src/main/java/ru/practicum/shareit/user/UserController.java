package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

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
    public User get(@PathVariable("id") Long userId) {
        log.info("Get user with id = " + userId);
        return userService.getById(userId);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Get all user");
        return userService.getAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Create new user");
        return userService.create(user);
    }

    @PatchMapping("/{id}")
    public User update(@Valid @PathVariable("id") Long userId, @RequestBody UserDto user) {
        log.info("Update user with id = " + userId);
        return userService.update(userId, user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long userId) {
        log.info("Delete user with id = " + userId);
        userService.delete(userId);
    }

}
