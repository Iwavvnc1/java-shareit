package ru.practicum.shareit.request.Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.InCorrectDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.Storage.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Test
    void create_whenInvoked_thenReturnRequest() {
        Long userId = 0L;
        Long requestId = 0L;
        User user = new User(userId);
        ItemRequest itemRequest = new ItemRequest(requestId, "description", user, LocalDateTime.now());
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(itemRequest)).thenReturn(itemRequest);
        ItemRequestCreateDto itemRequestCreateDto = itemRequestService.create(userId, itemRequest);
        assertEquals(itemRequest.getId(), itemRequestCreateDto.getId());
        assertEquals(itemRequest.getDescription(), itemRequestCreateDto.getDescription());
        verify(userRepository).findById(userId);
        verify(userRepository).existsUserById(userId);
        verify(itemRequestRepository).save(itemRequest);
    }

    @Test
    void create_whenInvokedWithFailedUser_thenReturnException() {
        Long userId = 0L;
        Long requestId = 0L;
        User user = new User(userId);
        ItemRequest itemRequest = new ItemRequest(requestId, "description", user, LocalDateTime.now());
        when(userRepository.existsUserById(userId)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> itemRequestService.create(userId, itemRequest));
        verify(userRepository, never()).findById(userId);
        verify(userRepository).existsUserById(userId);
        verify(itemRequestRepository, never()).save(itemRequest);
    }

    @Test
    void getAll_whenInvoked_thenReturnRequest() {
        Long userId = 0L;
        Long requestId = 0L;
        User user = new User(userId);
        ItemRequest request = new ItemRequest(requestId, "description", user, LocalDateTime.now());
        List<ItemRequest> requests = List.of(request);
        Item item = new Item(0L, "name", "description", true, user, request);
        List<Item> items = List.of(item);
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(itemRequestRepository.findAllByRequestorId(userId)).thenReturn(requests);
        when(itemRepository.findByRequestId(requestId)).thenReturn(items);
        List<ItemRequestWithItemsDto> returnRequest = itemRequestService.getAll(userId, false);
        assertEquals(requestId, returnRequest.get(0).getId());
        verify(userRepository).existsUserById(userId);
        verify(itemRequestRepository).findAllByRequestorId(userId);
        verify(itemRepository).findByRequestId(requestId);
    }

    @Test
    void getSort_whenInvoked_thenReturnEmptyRequest() {
        Long userId = 0L;
        Long requestId = 0L;
        Integer from = 1;
        Integer size = 1;
        Integer page = 1;
        User user = new User(userId);
        ItemRequest request = new ItemRequest(requestId, "description", user, LocalDateTime.now());
        List<ItemRequest> requests = List.of(request);
        Item item = new Item(0L, "name", "description", true, user, request);
        List<Item> items = List.of(item);
        PageImpl<ItemRequest> requestPage = new PageImpl<>(requests);
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(itemRequestRepository.findAll(PageRequest.of(page, size, Sort.by("created"))))
                .thenReturn(requestPage);
        when(itemRepository.findByRequestId(requestId)).thenReturn(items);
        List<ItemRequestWithItemsDto> returnRequest = itemRequestService.getSort(userId, from, size);
        assertTrue(returnRequest.isEmpty());
        verify(userRepository).existsUserById(userId);
        verify(itemRequestRepository).findAll(PageRequest.of(page, size, Sort.by("created")));
        verify(itemRepository).findByRequestId(requestId);
    }

    @Test
    void getSort_whenInvoked_thenReturnRequest() {
        Long userId = 0L;
        Long requestId = 0L;
        Integer from = null;
        Integer size = null;
        User user = new User(userId);
        ItemRequest request = new ItemRequest(requestId, "description", null, LocalDateTime.now());
        List<ItemRequest> requests = List.of(request);
        Item item = new Item(0L, "name", "description", true, user, request);
        List<Item> items = List.of(item);
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(itemRequestRepository.findAllByRequestorId(userId)).thenReturn(requests);
        when(itemRepository.findByRequestId(requestId)).thenReturn(items);
        List<ItemRequestWithItemsDto> returnRequest = itemRequestService.getSort(userId, from, size);
        assertFalse(returnRequest.isEmpty());
        verify(userRepository, times(2)).existsUserById(userId);
        verify(itemRequestRepository).findAllByRequestorId(userId);
        verify(itemRepository).findByRequestId(requestId);
    }

    @Test
    void getSort_whenInvokedWithIncorrectFromAndSize_thenReturnException() {
        Long userId = 0L;
        Long requestId = 0L;
        Integer from = -1;
        Integer size = -1;
        when(userRepository.existsUserById(userId)).thenReturn(true);
        assertThrows(InCorrectDataException.class, () -> itemRequestService.getSort(userId, from, size));
        verify(userRepository).existsUserById(userId);
        verify(itemRequestRepository, never()).findAllByRequestorId(userId);
        verify(itemRepository, never()).findByRequestId(requestId);
    }

    @Test
    void getById() {
        Long userId = 0L;
        Long requestId = 0L;
        User user = new User(userId);
        ItemRequest request = new ItemRequest(requestId, "description", user, LocalDateTime.now());
        Item item = new Item(0L, "name", "description", true, user, request);
        List<Item> items = List.of(item);
        when(userRepository.existsUserById(userId)).thenReturn(true);
        when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(request));
        when(itemRepository.findByRequestId(requestId)).thenReturn(items);
        ItemRequestWithItemsDto returnRequest = itemRequestService.getById(userId, requestId);
        List<ItemDto> itemsDto = returnRequest.getItems();
        assertEquals(requestId, returnRequest.getId());
        assertEquals(request.getDescription(), returnRequest.getDescription());
        assertEquals(request.getRequestor(), returnRequest.getRequestor());
        verify(userRepository).existsUserById(userId);
        verify(itemRequestRepository).findById(requestId);
        verify(itemRepository).findByRequestId(requestId);
    }
}