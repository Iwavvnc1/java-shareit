package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(Long userId, Item item);

    List<Item> getAll(Long userId);

    Item getById(Long itemId);

    Item update(Long userId, Long itemId, ItemDto item);

    List<Item> search(Long userId, String text);
}
