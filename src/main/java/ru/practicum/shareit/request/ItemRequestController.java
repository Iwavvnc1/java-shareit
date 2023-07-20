package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.Service.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestWithItemsDto>> getSort(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                 @RequestParam(required = false) Integer from,
                                                                 @RequestParam(required = false) Integer size) {
        log.info("Get itemRequests with id = " + userId);
        return ResponseEntity.ok(itemRequestService.getSort(userId, from, size));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestWithItemsDto> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                           @PathVariable("requestId") Long requestId) {
        log.info("Get itemRequest with id = " + requestId);
        return ResponseEntity.ok(itemRequestService.getById(userId, requestId));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestWithItemsDto>> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get all itemRequests with userId = " + userId);
        return ResponseEntity.ok(itemRequestService.getAll(userId, false));
    }

    @PostMapping
    public ResponseEntity<ItemRequestCreateDto> create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid
    @RequestBody ItemRequest itemRequest) {
        log.info("Create new itemRequest with userId = " + userId);
        return ResponseEntity.ok(itemRequestService.create(userId, itemRequest));
    }
}