package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;

    @Override
    public Item create(Long userId, Item item) {
        return itemStorage.create(userId, item);
    }

    @Override
    public List<Item> getAll(Long userId) {
        return itemStorage.getAll(userId);
    }

    @Override
    public Item getById(Long itemId) {
        return itemStorage.getById(itemId);
    }

    @Override
    public Item update(Long userId, Long itemId, ItemDto item) {
        return itemStorage.update(userId, itemId, item);
    }

    @Override
    public List<Item> search(Long userId, String text) {
        return itemStorage.search(userId, text);
    }
}
