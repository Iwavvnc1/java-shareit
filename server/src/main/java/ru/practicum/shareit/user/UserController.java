package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable("id") Long userId) {
        log.info("Get user with id = " + userId);
        return ResponseEntity.ok(userService.getById(userId));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        log.info("Get all user");
        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto user) {
        log.info("Create new user");
        return ResponseEntity.ok(userService.create(user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable("id") Long userId, @RequestBody UserDto user) {
        log.info("Update user with id = " + userId);
        return ResponseEntity.ok(userService.update(userId, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable("id") Long userId) {
        log.info("Delete user with id = " + userId);
        userService.delete(userId);
        return ResponseEntity.ok(userId);
    }

}
