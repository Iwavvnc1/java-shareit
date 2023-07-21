package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import org.hibernate.Hibernate;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequestCreateDto toItemRequestCreateDto(ItemRequest itemRequest) {
        return ItemRequestCreateDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequestWithItemsDto toItemRequestWithItemsDto(ItemRequest itemRequest) {
        return ItemRequestWithItemsDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(Hibernate.unproxy(itemRequest.getRequestor(), User.class))
                .created(itemRequest.getCreated())
                .build();
    }
}
