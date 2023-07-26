package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithRequestDto;

import javax.validation.Valid;

/**
 * TODO Sprint add-controllers.
 */
@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable("id") Long itemId) {
        log.info("Get item with id = " + itemId);
        return itemClient.getById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get all items with userId = " + userId);
        return itemClient.getAll(userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody @Valid ItemWithRequestDto item) {
        log.info("Create new item with userId = " + userId);
        return itemClient.create(userId, item);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Long itemId,
                                         @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody ItemDto item) {
        log.info("Update item with userId = " + userId);
        return itemClient.update(userId, itemId, item);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getSearch(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam String text) {
        log.info("Search items with text = " + text);
        return itemClient.search(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @Valid @RequestBody Comment comment,
                                                @PathVariable("itemId") Long itemId) {
        log.info("Create new comment with userId = " + userId);
        return itemClient.createComment(userId, itemId, comment);
    }
}