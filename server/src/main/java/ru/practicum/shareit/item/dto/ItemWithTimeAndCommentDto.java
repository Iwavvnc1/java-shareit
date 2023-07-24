package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingIdOutDto;

import java.util.List;


@Data
@Builder(toBuilder = true)
public class ItemWithTimeAndCommentDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingIdOutDto lastBooking;
    private BookingIdOutDto nextBooking;
    private List<CommentDto> comments;
}
