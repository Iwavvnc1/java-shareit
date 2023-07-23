package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.InCorrectDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.Storage.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.dto.BookingMapper.toBookingIdOutDto;
import static ru.practicum.shareit.item.dto.CommentMapper.toCommentDto;
import static ru.practicum.shareit.item.dto.ItemMapper.*;

@RequiredArgsConstructor
@Component
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Transactional
    @Override
    public ItemDto create(Long userId, ItemWithRequestDto itemDto) {
        existUser(userId);
        User user = userRepository.findById(userId).get();
        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest =
                    itemRequestRepository.findById(itemDto.getRequestId()).get();
            return toItemDto(itemRepository.save(toItemRequest(itemDto, user, itemRequest)));
        }
        Item item = toItem(itemDto, user);
        return toItemDto(itemRepository.save(item));
    }

    @Override
    public List<ItemWithTimeAndCommentDto> getAll(Long userId) {
        existUser(userId);
        List<ItemWithTimeAndCommentDto> items = new ArrayList<>();
        itemRepository.findAllByOwnerIdIs(userId).forEach(item -> items.add(getItemWithTimeAndCommentDto(item)));
        return items.stream().sorted(Comparator.comparing(ItemWithTimeAndCommentDto::getId)).collect(Collectors.toList());
    }

    @Override
    public ItemWithTimeAndCommentDto getById(Long userId, Long itemId) {
        existUser(userId);
        Item item = itemRepository.findById(itemId).get();
        if (!userId.equals(item.getOwner().getId())) {
            return getItemWithCommentDto(item);
        }
        return getItemWithTimeAndCommentDto(item);
    }

    @Transactional
    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto item) {
        existUser(userId);
        Item saveItem = itemRepository.findById(itemId).get();
        if (!userId.equals(saveItem.getOwner().getId())) {
            throw new NotFoundException("Item not found");
        }
        if (item.getName() != null) {
            saveItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            saveItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            saveItem.setAvailable(item.getAvailable());
        }
        return toItemDto(itemRepository.save(saveItem));
    }

    @Override
    public List<ItemDto> search(Long userId, String text) {
        if (text == null || text.length() == 0) {
            return new ArrayList<>();
        }
        return itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable(text,
                        text, true)
                .stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(Long userId, Long itemId, Comment comment) {
        existUser(userId);
        List<Booking> itemBookings = bookingRepository.findBookingByItemId(itemId);
        itemBookings = itemBookings.stream().filter(booking -> booking.getBooker().getId().equals(userId))
                .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now())).collect(Collectors.toList());
        if (itemBookings.size() == 0) {
            throw new InCorrectDataException("Booking not found.");
        }
        comment.setAuthor(userRepository.findById(userId).get());
        comment.setItem(itemRepository.findById(itemId).get());
        comment.setCreated(LocalDateTime.now());
        return toCommentDto(commentRepository.save(comment));
    }

    private void existUser(Long userId) {
        if (!userRepository.existsUserById(userId)) {
            throw new NotFoundException("User not found.");
        }
    }

    public ItemWithTimeAndCommentDto getItemWithTimeAndCommentDto(Item item) {
        Booking lastBooking = bookingRepository
                .getFirstByItemIdAndEndBeforeOrderByEndDesc(item.getId(), LocalDateTime.now());
        Booking nextBooking = bookingRepository
                .getTopByItemIdAndStartAfterOrderByStartAsc(item.getId(), LocalDateTime.now());
        List<CommentDto> itemComments = commentRepository.findCommentsByItemId(item.getId()).stream()
                .map(CommentMapper::toCommentDto).collect(Collectors.toList());
        if (nextBooking == null && lastBooking != null) {
            return toItemWithTimeDto(item, toBookingIdOutDto(lastBooking), null, itemComments);
        } else if (nextBooking != null && lastBooking == null) {
            return toItemWithTimeDto(item, null, toBookingIdOutDto(nextBooking), itemComments);
        } else if (nextBooking == null) {
            return toItemWithTimeDto(item, null, null, itemComments);
        } else {
            return toItemWithTimeDto(item, toBookingIdOutDto(lastBooking), toBookingIdOutDto(nextBooking),
                    itemComments);
        }
    }

    public ItemWithTimeAndCommentDto getItemWithCommentDto(Item item) {
        List<CommentDto> itemComments = commentRepository.findCommentsByItemId(item.getId()).stream()
                .map(CommentMapper::toCommentDto).collect(Collectors.toList());
        return ItemWithTimeAndCommentDto.builder()
                .id(item.getId())
                .available(item.getAvailable())
                .description(item.getDescription())
                .name(item.getName())
                .comments(itemComments)
                .build();
    }
}
