package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.request.Service.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    @Mock
    private ItemRequestService itemRequestService;
    @InjectMocks
    private ItemRequestController itemRequestController;

    @Test
    void getSort() {
        Long userId = 0L;
        Integer from = 0;
        Integer size = 0;
        List<ItemRequestWithItemsDto> requests = List.of(ItemRequestWithItemsDto.builder().build());
        when(itemRequestService.getSort(userId, from, size)).thenReturn(requests);
        ResponseEntity<List<ItemRequestWithItemsDto>> response = itemRequestController.getSort(userId, from, size);
        assertEquals(OK, response.getStatusCode());
        assertEquals(requests, response.getBody());

    }

    @Test
    void getById() {
        Long userId = 0L;
        Long requestId = 0L;
        ItemRequestWithItemsDto returnRequest = ItemRequestWithItemsDto.builder().build();
        when(itemRequestService.getById(userId, requestId)).thenReturn(returnRequest);
        ResponseEntity<ItemRequestWithItemsDto> response = itemRequestController.getById(userId, requestId);
        assertEquals(OK, response.getStatusCode());
        assertEquals(returnRequest, response.getBody());
    }

    @Test
    void getAll() {
        Long userId = 0L;
        List<ItemRequestWithItemsDto> requests = List.of(ItemRequestWithItemsDto.builder().build());
        when(itemRequestService.getAll(userId, false)).thenReturn(requests);
        ResponseEntity<List<ItemRequestWithItemsDto>> response = itemRequestController.getAll(userId);
        assertEquals(OK, response.getStatusCode());
        assertEquals(requests, response.getBody());
    }

    @Test
    void create() {
        Long userId = 0L;
        ItemRequestCreateDto returnRequest = ItemRequestCreateDto.builder().build();
        ItemRequest itemRequest = new ItemRequest();
        when(itemRequestService.create(userId, returnRequest)).thenReturn(returnRequest);
        ResponseEntity<ItemRequestCreateDto> response = itemRequestController.create(userId, returnRequest);
        assertEquals(OK, response.getStatusCode());
        assertEquals(returnRequest, response.getBody());
    }
}