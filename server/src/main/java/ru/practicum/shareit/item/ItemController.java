package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithRequestDto;
import ru.practicum.shareit.item.dto.ItemWithTimeAndCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{id}")
    public ResponseEntity<ItemWithTimeAndCommentDto> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                             @PathVariable("id") Long itemId) {
        log.info("Get item with id = " + itemId);
        return ResponseEntity.ok(itemService.getById(userId, itemId));
    }

    @GetMapping
    public ResponseEntity<List<ItemWithTimeAndCommentDto>> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get all items with userId = " + userId);
        return ResponseEntity.ok(itemService.getAll(userId));
    }

    @PostMapping
    public ResponseEntity<ItemDto> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestBody ItemWithRequestDto item) {
        log.info("Create new item with userId = " + userId);
        return ResponseEntity.ok(itemService.create(userId, item));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> update(@PathVariable("id") Long itemId,
                                          @RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestBody ItemDto item) {
        log.info("Update item with userId = " + userId);
        return ResponseEntity.ok(itemService.update(userId, itemId, item));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> getSearch(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam String text) {
        log.info("Search items with text = " + text);
        return ResponseEntity.ok(itemService.search(userId, text));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @RequestBody Comment comment,
                                                    @PathVariable("itemId") Long itemId) {
        log.info("Create new comment with userId = " + userId);
        return ResponseEntity.ok(itemService.createComment(userId, itemId, comment));
    }
}