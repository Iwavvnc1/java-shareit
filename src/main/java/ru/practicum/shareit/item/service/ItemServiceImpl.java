package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemWithTimeDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.dto.ItemMapper.toItemDto;
import static ru.practicum.shareit.item.dto.ItemMapper.toItemWithtimeDto;

@RequiredArgsConstructor
@Component
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ItemDto create(Long userId, Item item) {
        existUser(userId);
        item.setOwner(new User(userId));
        return toItemDto(itemRepository.save(item));
    }

    @Override
    public List<ItemWithTimeDto> getAll(Long userId) {
        existUser(userId);
        List<ItemWithTimeDto> items = new ArrayList<>();
        itemRepository.findAllByOwnerIdIs(userId).forEach(item -> items.add(getItemWithTimeDto(item)));
        return items.stream().sorted(Comparator.comparing(ItemWithTimeDto::getId)).collect(Collectors.toList());
    }

    @Override
    public ItemWithTimeDto getById(Long userId, Long itemId) {
        existUser(userId);
        Item item = itemRepository.findById(itemId).get();
        if(!userId.equals(item.getOwner().getId())) {
            return toItemWithtimeDto(item);
        }
        return getItemWithTimeDto(item);
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto item) {
        existUser(userId);
        Item saveItem = itemRepository.findById(itemId).get();
        if (!userId.equals(saveItem.getOwner().getId())) {
            throw new NotFoundException("Такого сочетания вещь/пользователь не существует");
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
        if(text == null || text.length() == 0) {
            return new ArrayList<>();
        }
        return itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailable
                        (text, text, true)
                .stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    private void existUser(Long userId) {
        if(!userRepository.existsUserById(userId)) {
            throw new NotFoundException("User not found.");
        }
    }
    public ItemWithTimeDto getItemWithTimeDto(Item item) {
        List<Booking> itemBookings = bookingRepository.findBookingByItemId(item.getId());
        if(itemBookings == null || itemBookings.size() == 0) {
            return toItemWithtimeDto(item);
        }
        return ItemWithTimeDto.builder()
                .id(item.getId())
                .available(item.getAvailable())
                .description(item.getDescription())
                .name(item.getName())
                .lastBooking(itemBookings.stream().filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .min(Comparator.comparing(Booking::getEnd)).map(BookingMapper::toBookingIdOutDto).get())
                .nextBooking(itemBookings.stream().filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .min(Comparator.comparing(Booking::getStart)).map(BookingMapper::toBookingIdOutDto).get())
                .build();
    }
}
