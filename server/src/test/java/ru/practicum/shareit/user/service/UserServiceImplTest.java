package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getById() {
        Long userId = 0L;
        User user = new User();
        UserDto returnUser = new UserDto();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        assertEquals(returnUser, userService.getById(userId));
    }

    @Test
    void getAll() {
        List<UserDto> returnUsers = List.of(new UserDto());
        List<User> users = List.of(new User());
        when(userRepository.findAll()).thenReturn(users);
        assertEquals(returnUsers, userService.getAll());
    }

    @Test
    void create() {
        User user = new User();
        UserDto returnUser = new UserDto();
        when(userRepository.save(user)).thenReturn(user);
        assertEquals(returnUser, userService.create(returnUser));
        verify(userRepository).save(user);
    }

    @Test
    void update_whenInvokedUpdateNameAndEmail_thenReturnUpdateUser() {
        Long userId = 0L;
        User oldUser = new User(0L, "name", "mail@mail.com");
        User newUser = new User(0L, "name1", "mail1@mail.com");
        UserDto returnUser = new UserDto(0L, "name1", "mail1@mail.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userRepository.save(newUser)).thenReturn(newUser);
        assertEquals(returnUser, userService.update(userId, returnUser));
        verify(userRepository).findById(userId);
        verify(userRepository).save(newUser);
    }

    @Test
    void update_whenInvokedUpdateEmail_thenReturnUpdateUser() {
        Long userId = 0L;
        User oldUser = new User(0L, "name", "mail@mail.com");
        User newUser = new User(0L, null, "mail1@mail.com");
        UserDto returnUser = new UserDto(0L, "name1", "mail1@mail.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userRepository.save(any())).thenReturn(newUser);
        UserDto savedUser = userService.update(userId, returnUser);
        assertEquals(returnUser.getId(), savedUser.getId());
        assertNull(savedUser.getName());
        assertEquals(returnUser.getEmail(), savedUser.getEmail());
        verify(userRepository).findById(userId);
        verify(userRepository).save(any());
    }

    @Test
    void update_whenInvokedUpdateName_thenReturnUpdateUser() {
        Long userId = 0L;
        User oldUser = new User(0L, "name", "mail@mail.com");
        User newUser = new User(0L, "name1", null);
        UserDto returnUser = new UserDto(0L, "name1", "mail1@mail.com");
        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userRepository.save(any())).thenReturn(newUser);
        UserDto savedUser = userService.update(userId, returnUser);
        assertEquals(returnUser.getId(), savedUser.getId());
        assertEquals(returnUser.getName(), savedUser.getName());
        assertNull(savedUser.getEmail());
        verify(userRepository).findById(userId);
        verify(userRepository).save(any());
    }

    @Test
    void delete() {
        Long userId = 0L;
        userService.delete(userId);
        verify(userRepository).deleteById(userId);
    }
}