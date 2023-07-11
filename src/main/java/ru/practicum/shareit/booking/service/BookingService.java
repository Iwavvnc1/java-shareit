package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;

import java.util.List;

public interface BookingService {
    BookingOutDto create(Long userId, BookingInDto bookingDto);


    BookingOutDto getById(Long userId, Long bookingId);

    BookingOutDto updateStatus(Long userId, Long bookingId, Boolean status);

    List<BookingOutDto> getAllByUser(Long userId, String state);

    List<BookingOutDto> getAllByItem(Long userId, String state);
}
