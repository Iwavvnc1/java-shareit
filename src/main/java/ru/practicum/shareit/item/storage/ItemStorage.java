package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    ItemDto create(Long userId, Item item);

    List<ItemDto> getAll(Long userId);

    ItemDto getById(Long itemId);

    ItemDto update(Long userId, Long itemId, ItemDto item);

    List<ItemDto> search(Long userId, String text);
}
