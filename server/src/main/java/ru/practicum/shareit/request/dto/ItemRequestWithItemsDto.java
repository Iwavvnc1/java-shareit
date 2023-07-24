package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class ItemRequestWithItemsDto {
    private Long id;
    private String description;
    @ManyToOne
    private User requestor;
    private LocalDateTime created;
    private List<ItemDto> items;

    public List<ItemDto> getItems() {
        return items;
    }

}
