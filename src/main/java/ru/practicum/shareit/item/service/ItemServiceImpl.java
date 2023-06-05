package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.List;

@Component
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    @Override
    public ItemDto create(Long userId, Item item) {
        return itemStorage.create(userId, item);
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        return itemStorage.getAll(userId);
    }

    @Override
    public ItemDto getById(Long itemId) {
        return itemStorage.getById(itemId);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto item) {
        return itemStorage.update(userId, itemId, item);
    }

    @Override
    public List<ItemDto> search(Long userId, String text) {
        return itemStorage.search(userId, text);
    }
}
