package ru.practicum.shareit.request.Service;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestCreateDto create(Long userId, ItemRequestCreateDto itemRequestDto);

    List<ItemRequestWithItemsDto> getAll(Long userId, boolean filter);

    List<ItemRequestWithItemsDto> getSort(Long userId, Integer from, Integer size);

    ItemRequestWithItemsDto getById(Long userId, Long requestId);
}
