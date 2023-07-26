package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithRequestDto;
import ru.practicum.shareit.item.dto.ItemWithTimeAndCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Mock
    private ItemService itemService;
    @InjectMocks
    private ItemController itemController;

    @Test
    void getById() {
        Long userId = 0L;
        Long itemId = 0L;
        ItemWithTimeAndCommentDto item = ItemWithTimeAndCommentDto.builder().build();
        when(itemService.getById(userId, itemId)).thenReturn(item);
        ResponseEntity<ItemWithTimeAndCommentDto> response = itemController.getById(userId, itemId);
        assertEquals(OK, response.getStatusCode());
        assertEquals(item, response.getBody());
    }

    @Test
    void getAll() {
        Long userId = 0L;
        List<ItemWithTimeAndCommentDto> items = List.of(ItemWithTimeAndCommentDto.builder().build());
        when(itemService.getAll(userId)).thenReturn(items);
        ResponseEntity<List<ItemWithTimeAndCommentDto>> response = itemController.getAll(userId);
        assertEquals(OK, response.getStatusCode());
        assertEquals(items, response.getBody());
    }

    @Test
    void create() {
        Long userId = 0L;
        ItemWithRequestDto item = ItemWithRequestDto.builder().build();
        ItemDto returnItem = ItemDto.builder().build();
        when(itemService.create(userId, item)).thenReturn(returnItem);
        ResponseEntity<ItemDto> response = itemController.create(userId, item);
        assertEquals(OK, response.getStatusCode());
        assertEquals(returnItem, response.getBody());
    }

    @Test
    void update() {
        Long userId = 0L;
        Long itemId = 0L;
        ItemDto item = ItemDto.builder().build();
        when(itemService.update(userId, itemId, item)).thenReturn(item);
        ResponseEntity<ItemDto> response = itemController.update(userId, itemId, item);
        assertEquals(OK, response.getStatusCode());
        assertEquals(item, response.getBody());
    }

    @Test
    void getSearch() {
        Long userId = 0L;
        List<ItemDto> items = List.of(ItemDto.builder().build());
        String searchText = "text";
        when(itemService.search(userId, searchText)).thenReturn(items);
        ResponseEntity<List<ItemDto>> response = itemController.getSearch(userId, searchText);
        assertEquals(OK, response.getStatusCode());
        assertEquals(items, response.getBody());
    }

    @Test
    void createComment() {
        Long userId = 0L;
        Long itemId = 0L;
        Comment comment = new Comment();
        CommentDto returnComment = CommentDto.builder().build();
        when(itemService.createComment(userId, itemId, comment)).thenReturn(returnComment);
        ResponseEntity<CommentDto> response = itemController.createComment(userId, comment, itemId);
        assertEquals(OK, response.getStatusCode());
        assertEquals(returnComment, response.getBody());

    }
}