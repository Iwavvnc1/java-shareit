package ru.practicum.shareit.request.Service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InCorrectDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.Storage.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.practicum.shareit.item.dto.ItemMapper.toItemDto;
import static ru.practicum.shareit.request.dto.ItemRequestMapper.toItemRequestCreateDto;
import static ru.practicum.shareit.request.dto.ItemRequestMapper.toItemRequestWithItemsDto;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestCreateDto create(Long userId, ItemRequest itemRequest) {
        existUser(userId);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(userRepository.findById(userId).get());
        return toItemRequestCreateDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestWithItemsDto> getAll(Long userId, boolean filter) {
        existUser(userId);
        return getItemRequests(itemRequestRepository.findAllByRequestorId(userId).stream(), userId, filter);
    }

    @Override
    public List<ItemRequestWithItemsDto> getSort(Long userId, Integer from, Integer size) {
        existUser(userId);
        if (from == null || size == null) {
            return getAll(userId, true);
        }
        if (from < 0 || size <= 0) {
            throw new InCorrectDataException("Incorrect data.");
        }
        int page = from / size;
        return new PageImpl<>(getItemRequests(itemRequestRepository
                .findAll(PageRequest.of(page, size, Sort.by("created"))).stream(), userId, true), PageRequest
                .of(page, size, Sort.by("created")), size.longValue()).getContent();
    }

    @Override
    public ItemRequestWithItemsDto getById(Long userId, Long requestId) {
        existUser(userId);
        return toItemRequestWithItemsDto(itemRequestRepository.findById(requestId).get()).toBuilder()
                .items(itemRepository.findByRequestId(requestId).stream()
                        .map(ItemMapper::toItemDto)
                        .collect(Collectors.toList()))
                .build();
    }

    private void existUser(Long userId) {
        if (!userRepository.existsUserById(userId)) {
            throw new NotFoundException("User not found.");
        }
    }

    private List<ItemRequestWithItemsDto> getItemRequests(Stream<ItemRequest> stream, Long userId, boolean filter) {
        List<ItemRequestWithItemsDto> itemRequests = new ArrayList<>();
        stream.map(ItemRequestMapper::toItemRequestWithItemsDto)
                .forEach(itemRequestWithItemsDto -> {
                    List<ItemDto> items = new ArrayList<>();
                    itemRepository.findByRequestId(itemRequestWithItemsDto.getId())
                            .forEach(item -> {
                                item.setRequest(Hibernate.unproxy(item.getRequest(), ItemRequest.class));
                                item.getRequest()
                                        .setRequestor(Hibernate.unproxy(item.getRequest()
                                                .getRequestor(), User.class));
                                item.setOwner(Hibernate.unproxy(item.getOwner(), User.class));
                                items.add(toItemDto(item));
                            });
                    ItemRequestWithItemsDto itemRequest = itemRequestWithItemsDto.toBuilder()
                            .items(items)
                            .build();
                    if (filter) {
                        if (itemRequest.getRequestor() == null || !itemRequest.getRequestor().getId().equals(userId)) {
                            itemRequests.add(itemRequest);
                        }
                        return;
                    }
                    itemRequests.add(itemRequest);
                });
        return itemRequests;
    }
}
