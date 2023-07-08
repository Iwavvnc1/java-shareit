package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithTimeAndCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, Item item);

    List<ItemWithTimeAndCommentDto> getAll(Long userId);

    ItemWithTimeAndCommentDto getById(Long userId, Long itemId);

    ItemDto update(Long userId, Long itemId, ItemDto item);

    List<ItemDto> search(Long userId, String text);

    CommentDto createComment(Long userId, Long itemId, Comment comment);
}
