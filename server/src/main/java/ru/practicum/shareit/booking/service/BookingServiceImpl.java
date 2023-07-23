package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import java.util.stream.Stream;

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
            return BookingMapper.toBookingOutDto(bookingRepository.save(booking));
        } else {
            booking.setStatus(Status.REJECTED);
            return BookingMapper.toBookingOutDto(bookingRepository.save(booking));
        }
    }

    @Override
    public List<BookingOutDto> getAllByItem(Long userId, String state, Integer from, Integer size) {
        if (size != null && size <= 0 || from != null && from < 0) {
            throw new InCorrectDataException("Incorrect data.");
        }
        existUser(userId);
        List<Long> itemIds = new ArrayList<>();
        itemRepository.findAllByOwnerIdIs(userId).forEach(item -> itemIds.add(item.getId()));
        Stream<Booking> bookings;
        Integer page = null;
        if (size != null && from != null) {
            page = from / size;
        }
        if (size != null && page != null) {
            bookings = bookingRepository.findByItemIdIn(itemIds, PageRequest.of(page, size, Sort.by("id")
                            .descending()))
                    .stream();
        } else {
            bookings = bookingRepository.findByItemIdIn(itemIds).stream();
        }
        return getAllByState(bookings, state, page, size);
    }

    @Override
    public List<BookingOutDto> getAllByUser(Long userId, String state, Integer from, Integer size) {
        if (size != null && size <= 0 || from != null && from < 0) {
            throw new InCorrectDataException("Incorrect data.");
        }
        existUser(userId);
        Integer page = null;
        if (size != null && from != null) {
            page = from / size;
        }
        Stream<Booking> bookings;
        if (size != null && page != null) {
            bookings = bookingRepository.findByBookerId(userId, PageRequest.of(page, size, Sort.by("id")
                            .descending()))
                    .stream();
        } else {
            bookings = bookingRepository.findByBookerId(userId).stream();
        }
        return getAllByState(bookings, state, page, size);
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
        return BookingMapper.toBookingOutDto(bookingRepository.save(BookingMapper.toBooking(bookingDto, user,
                item, Status.WAITING)));
    }

    @Override
    public BookingOutDto getById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).get();
        if (userId.equals(booking.getBooker().getId()) || userId.equals(booking.getItem().getOwner().getId())) {
            return BookingMapper.toBookingOutDto(booking);
        } else {
            throw new NotFoundException("NotFound.");
        }
    }

    private void existUser(Long userId) {
        if (!userRepository.existsUserById(userId)) {
            throw new NotFoundException("User not found.");
        }
    }

    private List<BookingOutDto> getAllByState(Stream<Booking> bookings, String state, Integer page, Integer size) {
        if ((state == null || state.equals("ALL")) && (page == null || size == null)) {
            return bookings.sorted(Comparator.comparing(Booking::getStart).reversed())
                    .map(BookingMapper::toBookingOutDto).collect(Collectors.toList());
        }
        if (state == null || state.equals("ALL")) {
            return new PageImpl<>(bookings.sorted(Comparator.comparing(Booking::getStart).reversed())
                    .map(BookingMapper::toBookingOutDto).collect(Collectors.toList()),
                    PageRequest.of(page, size, Sort.by("id").ascending()), size.longValue()).getContent();
        }
        switch (state) {
            case ("CURRENT"):
                bookings = bookings.filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                        .filter(booking -> booking.getEnd().isAfter(LocalDateTime.now()));
                break;
            case ("PAST"):
                bookings = bookings.filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                        .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()));
                break;
            case ("FUTURE"):
                bookings = bookings.filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .filter(booking -> booking.getEnd().isAfter(LocalDateTime.now()));
                break;
            case ("WAITING"):
                bookings = bookings.filter(booking -> booking.getStatus().equals(Status.WAITING));
                break;
            case ("REJECTED"):
                bookings = bookings.filter(booking -> booking.getStatus().equals(Status.REJECTED));
                break;
            default:
                throw new InCorrectDataException("Unknown state: UNSUPPORTED_STATUS");
        }
        if (page != null && size != null) {
            return new PageImpl<>(bookings.sorted(Comparator.comparing(Booking::getStart).reversed())
                    .map(BookingMapper::toBookingOutDto)
                    .collect(Collectors.toList()), PageRequest.of(page, size), size.longValue()).getContent();
        }
        return bookings.sorted(Comparator.comparing(Booking::getStart).reversed())
                .map(BookingMapper::toBookingOutDto)
                .collect(Collectors.toList());
    }
}
