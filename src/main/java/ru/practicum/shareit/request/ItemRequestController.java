package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.Service.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @GetMapping("/all")/*/all?from={from}&size={size}*/
    public List<ItemRequestWithItemsDto> getSort(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(required = false) Integer from,
                                                 @RequestParam(required = false) Integer size) {
        log.info("Get itemRequests with id = " + userId);
        return itemRequestService.getSort(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithItemsDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @PathVariable("requestId") Long requestId) {
        log.info("Get itemRequest with id = " + requestId);
        return itemRequestService.getById(userId, requestId);
    }

    @GetMapping
    public List<ItemRequestWithItemsDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get all itemRequests with userId = " + userId);
        return itemRequestService.getAll(userId, false);
    }

    @PostMapping
    public ItemRequestCreateDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid
    @RequestBody ItemRequest itemRequest) {
        log.info("Create new itemRequest with userId = " + userId);
        return itemRequestService.create(userId, itemRequest);
    }
}
/*-POST /requests — добавить новый запрос вещи. Основная часть запроса — текст запроса, где пользователь описывает,
какая именно вещь ему нужна.
-GET /requests — получить список своих запросов вместе с данными об ответах на них.
Для каждого запроса должны указываться описание, дата и время создания и список ответов в формате:
id вещи, название, её описание description, а также requestId запроса и признак доступности вещи available.
Так в дальнейшем, используя указанные id вещей, можно будет получить подробную информацию о каждой вещи.
Запросы должны возвращаться в отсортированном порядке от более новых к более старым.
-GET /requests/all?from={from}&size={size} — получить список запросов, созданных другими пользователями.
С помощью этого эндпоинта пользователи смогут просматривать существующие запросы, на которые они могли бы ответить.
Запросы сортируются по дате создания: от более новых к более старым. Результаты должны возвращаться постранично.
Для этого нужно передать два параметра: from — индекс первого элемента,
начиная с 0, и size — количество элементов для отображения.
-GET /requests/{requestId} — получить данные об одном конкретном запросе вместе с данными об ответах на него
в том же формате, что и в эндпоинте GET /requests. Посмотреть данные об отдельном запросе может любой пользователь.*/
