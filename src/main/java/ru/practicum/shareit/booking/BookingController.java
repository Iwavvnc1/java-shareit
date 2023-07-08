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

    @GetMapping/*/bookings?state={state}*/
    public List<BookingOutDto> getAllByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam(required = false) String state) {
        log.info("Get all bookings by user with userId = " + userId);
        return bookingService.getAllByUser(userId, state);
    }

    @GetMapping("/{bookingId}")/*bookingId*/
    public BookingOutDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable("bookingId") Long bookingId) {
        log.info("Get by id bookings with userId = " + userId);
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping("/owner")/*/owner?state={state}*/
    public List<BookingOutDto> getAllByItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(required = false) String state) {
        log.info("Get all bookings by items with userId = " + userId);
        return bookingService.getAllByItem(userId, state);
    }
}
/*Добавление нового запроса на бронирование. Запрос может быть создан любым пользователем,
а затем подтверждён владельцем вещи. Эндпоинт — POST /bookings.
После создания запрос находится в статусе WAITING — «ожидает подтверждения».
Подтверждение или отклонение запроса на бронирование.
Может быть выполнено только владельцем вещи. Затем статус бронирования становится либо APPROVED, либо REJECTED.
 Эндпоинт — PATCH /bookings/{bookingId}?approved={approved}, параметр approved может принимать значения true или false.
Получение данных о конкретном бронировании (включая его статус).
Может быть выполнено либо автором бронирования, либо владельцем вещи, к которой относится бронирование.
Эндпоинт — GET /bookings/{bookingId}.
Получение списка всех бронирований текущего пользователя.
Эндпоинт — GET /bookings?state={state}. Параметр state необязательный и по умолчанию равен ALL (англ. «все»).
Также он может принимать значения CURRENT (англ. «текущие»), **PAST** (англ. «завершённые»), FUTURE (англ. «будущие»),
WAITING (англ. «ожидающие подтверждения»), REJECTED (англ. «отклонённые»).
Бронирования должны возвращаться отсортированными по дате от более новых к более старым.
Получение списка бронирований для всех вещей текущего пользователя.
Эндпоинт — GET /bookings/owner?state={state}. Этот запрос имеет смысл для владельца хотя бы одной вещи.
Работа параметра state аналогична его работе в предыдущем сценарии.*/