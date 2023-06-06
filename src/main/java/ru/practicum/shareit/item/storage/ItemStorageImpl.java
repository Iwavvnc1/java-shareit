package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ItemStorageImpl implements ItemStorage {
    private final UserStorage userStorage;
    private final AtomicInteger idItem = new AtomicInteger(1);
    private final Map<Long, Item> items = new HashMap<>();

    @Autowired
    public ItemStorageImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public ItemDto create(Long userId, Item item) {
        if (userStorage.getAll().stream().anyMatch(user -> user.getId().equals(userId))) {
            item = item.toBuilder()
                    .id(idItem.longValue())
                    .description(item.getDescription())
                    .name(item.getName())
                    .owner(userStorage.getById(userId))
                    .build();
            items.put(item.getId(), item);
            idItem.addAndGet(1);
            return ItemMapper.toItemDto(item);
        } else {
            log.error("User with id = " + userId + " not found");
            throw new NotFoundException("пользователь не найден");
        }
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        List<ItemDto> itemsDto = new ArrayList<>();
        items.values().stream().filter(item -> item.getOwner().getId().equals(userId))
                .forEach(item -> itemsDto.add(ItemMapper.toItemDto(item)));
        return itemsDto;
    }

    @Override
    public ItemDto getById(Long itemId) {
        return ItemMapper.toItemDto(items.get(itemId));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto item) {
        if (userStorage.getAll().stream().anyMatch(user -> user.getId().equals(userId))) {
            if (userStorage.getAll().stream()
                    .filter(user -> user.getId().equals(userId))
                    .anyMatch(user -> user.getId().equals(items.get(itemId).getOwner().getId()))) {
                Item updateItem = items.get(itemId);
                if (item.getName() != null) {
                    updateItem = updateItem.toBuilder().name(item.getName()).build();
                }
                if (item.getDescription() != null) {
                    updateItem = updateItem.toBuilder().description(item.getDescription()).build();
                }
                if (item.getAvailable() != null) {
                    updateItem = updateItem.toBuilder().available(item.getAvailable()).build();
                }
                items.put(updateItem.getId(), updateItem);
                return ItemMapper.toItemDto(updateItem);
            } else {
                log.error("User with id = " + userId + " haven`t item with id = " + itemId);
                throw new NotFoundException("Пользователь не имеет такой вещи.");
            }
        } else {
            log.error("User with id = " + userId + " not found");
            throw new NotFoundException("пользователь не найден");
        }
    }

    @Override
    public List<ItemDto> search(Long userId, String text) {
        if (text.length() < 1) return new ArrayList<>();
        return items.values().stream()
                .filter(item -> item.getAvailable().equals(true))
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
