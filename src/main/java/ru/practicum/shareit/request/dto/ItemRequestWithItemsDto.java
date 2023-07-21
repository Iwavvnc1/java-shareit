package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder(toBuilder = true)
public class ItemRequestWithItemsDto {
    Long id;
    String description;
    @ManyToOne
    User requestor;
    LocalDateTime created;
    List<ItemDto> items;

    public List<ItemDto> getItems() {
        return items;
    }

}
