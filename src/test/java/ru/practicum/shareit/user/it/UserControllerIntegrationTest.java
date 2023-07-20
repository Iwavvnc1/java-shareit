package ru.practicum.shareit.user.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @SneakyThrows
    @Test
    void getById() {
        long userId = 1L;
        mockMvc.perform(get("/users/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk());
        verify(userService).getById(userId);
    }

    @SneakyThrows
    @Test
    void getAll() {
        long userId = 1L;
        UserDto returnUser = new UserDto(userId, "name", "mail@mail.com");
        List<UserDto> users = List.of(returnUser);
        when(userService.getAll()).thenReturn(users);
        String result = mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(users), result);
        verify(userService).getAll();
    }

    @SneakyThrows
    @Test
    void create_WithFailedUser_thenReturnBadRequest() {
        long userId = 1L;
        User user = new User(userId, "name", "mail");
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(userService, never()).create(user);
    }

    @SneakyThrows
    @Test
    void create_WithValidUser_thenReturnUser() {
        long userId = 1L;
        User user = new User(userId, "name", "mail@mail.com");
        UserDto returnUser = new UserDto(userId, "name", "mail@mail.com");
        when(userService.create(any())).thenReturn(returnUser);
        String result = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(user), result);
        verify(userService).create(any());
    }

    @SneakyThrows
    @Test
    void update_WithValidUser_thenReturnUser() {
        long userId = 1L;
        User user = new User(userId, "name", "mail@mail.com");
        UserDto returnUser = new UserDto(userId, "name", "mail@mail.com");
        when(userService.update(anyLong(), any())).thenReturn(returnUser);
        String result = mockMvc.perform(patch("/users/{id}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(user), result);
        verify(userService).update(anyLong(), any());
    }

    @SneakyThrows
    @Test
    void update_WithFailedUser_thenReturnBadRequest() {
        long userId = 1L;
        User user = new User(userId, "name", "mail");
        mockMvc.perform(patch("/users/{id}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        verify(userService, never()).create(user);
    }

    @SneakyThrows
    @Test
    void delete_whenInvoked_thenReturnOk() {
        long userId = 1L;
        mockMvc.perform(delete("/users/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk());
        verify(userService).delete(userId);
    }
}