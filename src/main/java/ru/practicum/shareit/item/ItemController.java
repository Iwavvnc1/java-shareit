package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithRequestDto;
import ru.practicum.shareit.item.dto.ItemWithTimeAndCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
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
    public ItemWithTimeAndCommentDto get(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("id") Long itemId) {
        log.info("Get item with id = " + itemId);
        return itemService.getById(userId, itemId);
    }

    @GetMapping
    public List<ItemWithTimeAndCommentDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get all items with userId = " + userId);
        return itemService.getAll(userId);
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid ItemWithRequestDto item) {
        log.info("Create new item with userId = " + userId);
        return itemService.create(userId, item);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable("id") Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId,
                          @Valid @RequestBody ItemDto item) {
        log.info("Update item with userId = " + userId);
        return itemService.update(userId, itemId, item);
    }

    @GetMapping("/search")
    public List<ItemDto> get(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestParam String text) {
        log.info("Search items with text = " + text);
        return itemService.search(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody Comment comment,
                                    @PathVariable("itemId") Long itemId) {
        log.info("Create new comment with userId = " + userId);
        return itemService.createComment(userId, itemId, comment);
    }
}