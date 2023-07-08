package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithTimeDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, Item item);

    List<ItemWithTimeDto> getAll(Long userId);

    ItemWithTimeDto getById(Long userId, Long itemId);

    ItemDto update(Long userId, Long itemId, ItemDto item);

    List<ItemDto> search(Long userId, String text);
}
