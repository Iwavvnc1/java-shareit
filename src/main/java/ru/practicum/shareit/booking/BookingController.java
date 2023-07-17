package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingOutDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody BookingInDto bookingDto) {
        log.info("Create new booking with userId = " + userId);
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingOutDto updateStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @PathVariable("bookingId") Long bookingId, @RequestParam Boolean approved) {
        log.info("update status booking with id " + bookingId + " with userId = " + userId);
        return bookingService.updateStatus(userId, bookingId, approved);
    }

    @GetMapping
    public List<BookingOutDto> getAllByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam(required = false) String state,
                                            @RequestParam(required = false) Integer from,
                                            @RequestParam(required = false) Integer size) {
        log.info("Get all bookings by user with userId = " + userId);
        return bookingService.getAllByUser(userId, state, from, size);
    }

    @GetMapping("/{bookingId}")
    public BookingOutDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable("bookingId") Long bookingId) {
        log.info("Get by id bookings with userId = " + userId);
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping("/owner")
    public List<BookingOutDto> getAllByItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(required = false) String state,
                                             @RequestParam(required = false) Integer from,
                                             @RequestParam(required = false) Integer size) {
        log.info("Get all bookings by items with userId = " + userId);
        return bookingService.getAllByItem(userId, state, from, size);
    }
}