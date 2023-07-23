package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

import javax.validation.Valid;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @GetMapping("/all")
    public ResponseEntity<Object> getSort(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestParam(required = false) Integer from,
                                          @RequestParam(required = false) Integer size) {
        log.info("Get itemRequests with id = " + userId);
        return itemRequestClient.getSort(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable("requestId") Long requestId) {
        log.info("Get itemRequest with id = " + requestId);
        return itemRequestClient.getById(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get all itemRequests with userId = " + userId);
        return itemRequestClient.getAll(userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid
    @RequestBody ItemRequestCreateDto itemRequest) {
        log.info("Create new itemRequest with userId = " + userId);
        return itemRequestClient.create(userId, itemRequest);
    }
}