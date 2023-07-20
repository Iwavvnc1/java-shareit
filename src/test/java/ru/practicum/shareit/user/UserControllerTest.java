package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.user.dto.UserMapper.toUserDto;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    @Test
    void getById_whenInvoked_thenReturnUser() {
        Long userId = 0L;
        UserDto expectUser = new UserDto();
        when(userService.getById(userId)).thenReturn(expectUser);
        ResponseEntity<UserDto> response = userController.getById(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectUser, response.getBody());
    }

    @Test
    void getAll_whenInvoked_thenReturnUsers() {
        List<UserDto> expectUsers = List.of(new UserDto());
        when(userService.getAll()).thenReturn(expectUsers);
        ResponseEntity<List<UserDto>> response = userController.getAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectUsers, response.getBody());
    }

    @Test
    void create_whenInvoked_thenReturnSaveUser() {
        User newUser = new User();
        UserDto returnUserDto = toUserDto(newUser);
        when(userService.create(newUser)).thenReturn(returnUserDto);
        ResponseEntity<UserDto> response = userController.create(newUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(returnUserDto, response.getBody());
    }

    @Test
    void update() {
        Long userId = 0L;
        User newUser = new User();
        newUser.setId(userId);
        newUser.setName("name");
        newUser.setEmail("mail@mail.com");
        User oldUser = new User();
        oldUser.setId(userId);
        oldUser.setName("name1");
        oldUser.setEmail("mail1@mail.com");
        UserDto newUserDto = toUserDto(newUser);
        when(userService.update(userId, oldUser)).thenReturn(newUserDto);
        ResponseEntity<UserDto> response = userController.update(userId, oldUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newUserDto, response.getBody());
    }

    @Test
    void delete() {
        Long userId = 0L;
        ResponseEntity<Long> response = userController.delete(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}