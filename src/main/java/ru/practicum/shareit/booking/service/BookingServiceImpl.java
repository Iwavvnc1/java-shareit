package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.InCorrectDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.dto.BookingMapper.*;

@RequiredArgsConstructor
@Component
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    @Override
    public BookingOutDto updateStatus(Long userId, Long bookingId, Boolean status) {
        Booking booking = bookingRepository.findById(bookingId).get();
        if (!userId.equals(booking.getItem().getOwner().getId())) {
            throw new NotFoundException("Not found");
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new InCorrectDataException("It`s already approved.");
        }
        if (status) {
            booking.setStatus(Status.APPROVED);
            return toBookingOutDto(bookingRepository.save(booking));
        } else {
            booking.setStatus(Status.REJECTED);
            return toBookingOutDto(bookingRepository.save(booking));
        }
    }

    @Override
    public List<BookingOutDto> getAllByItem(Long userId, String state) {
        existUser(userId);
        List<Long> itemIds = new ArrayList<>();
        itemRepository.findAllByOwnerIdIs(userId).stream().forEach(item -> itemIds.add(item.getId()));
        List<Booking> bookings = bookingRepository.findByItemIdIn(itemIds);
        return getAllByState(bookings, state);
    }

    @Override
    public List<BookingOutDto> getAllByUser(Long userId, String state) {
        existUser(userId);
        List<Booking> bookings = bookingRepository.findByBookerId(userId);
        return getAllByState(bookings, state);
    }

    @Override
    public BookingOutDto create(Long userId, BookingInDto bookingDto) {
        existUser(userId);
        User user = userRepository.findById(userId).get();
        Item item = itemRepository.findById(bookingDto.getItemId()).get();
        if (!item.getAvailable()) {
            throw new InCorrectDataException("item is not available.");
        }
        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("");
        }
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null ||
                bookingDto.getStart().equals(bookingDto.getEnd()) || bookingDto.getStart().isAfter(bookingDto.getEnd())
                || bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new InCorrectDataException("Incorrect time.");
        }
        return toBookingOutDto(bookingRepository.save(toBooking(bookingDto, user,
                item, Status.WAITING)));
    }

    @Override
    public BookingOutDto getById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).get();
        if (userId.equals(booking.getBooker().getId()) || userId.equals(booking.getItem().getOwner().getId())) {
            return toBookingOutDto(booking);
        } else {
            throw new NotFoundException("NotFound.");
        }
    }

    private void existUser(Long userId) {
        if (!userRepository.existsUserById(userId)) {
            throw new NotFoundException("User not found.");
        }
    }

    private List<BookingOutDto> getAllByState(List<Booking> bookings, String state) {
        if (state == null || state.equals("ALL")) {
            return bookings.stream().sorted(Comparator.comparing(Booking::getStart).reversed())
                    .map(BookingMapper::toBookingOutDto).collect(Collectors.toList());
        }
        switch (state) {
            case ("CURRENT"):
                bookings = bookings.stream().filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                        .filter(booking -> booking.getEnd().isAfter(LocalDateTime.now())).collect(Collectors.toList());
                break;
            case ("PAST"):
                bookings = bookings.stream().filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                        .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
                break;
            case ("FUTURE"):
                bookings = bookings.stream().filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .filter(booking -> booking.getEnd().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
                break;
            case ("WAITING"):
                bookings = bookings.stream().filter(booking -> booking.getStatus().equals(Status.WAITING))
                        .collect(Collectors.toList());
                break;
            case ("REJECTED"):
                bookings = bookings.stream().filter(booking -> booking.getStatus().equals(Status.REJECTED))
                        .collect(Collectors.toList());
                break;
            default:
                throw new InCorrectDataException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream().sorted(Comparator.comparing(Booking::getStart).reversed())
                .map(BookingMapper::toBookingOutDto).collect(Collectors.toList());
    }
}
