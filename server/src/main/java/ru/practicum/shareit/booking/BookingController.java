package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.service.BookingService;

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
    public ResponseEntity<BookingOutDto> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestBody BookingInDto bookingDto) {
        log.info("Create new booking with userId = " + userId);
        return ResponseEntity.ok(bookingService.create(userId, bookingDto));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingOutDto> updateStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @PathVariable("bookingId") Long bookingId, @RequestParam Boolean approved) {
        log.info("update status booking with id " + bookingId + " with userId = " + userId);
        return ResponseEntity.ok(bookingService.updateStatus(userId, bookingId, approved));
    }

    @GetMapping
    public ResponseEntity<List<BookingOutDto>> getAllByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                            @RequestParam(required = false) String state,
                                                            @RequestParam(required = false) Integer from,
                                                            @RequestParam(required = false) Integer size) {
        log.info("Get all bookings by user with userId = " + userId);
        return ResponseEntity.ok(bookingService.getAllByUser(userId, state, from, size));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingOutDto> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable("bookingId") Long bookingId) {
        log.info("Get by id bookings with userId = " + userId);
        return ResponseEntity.ok(bookingService.getById(userId, bookingId));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingOutDto>> getAllByItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                             @RequestParam(required = false) String state,
                                                             @RequestParam(required = false) Integer from,
                                                             @RequestParam(required = false) Integer size) {
        log.info("Get all bookings by items with userId = " + userId);
        return ResponseEntity.ok(bookingService.getAllByItem(userId, state, from, size));
    }
}