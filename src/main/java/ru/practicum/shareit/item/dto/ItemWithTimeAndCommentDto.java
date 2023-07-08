package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.booking.dto.BookingIdOutDto;

import java.util.List;


@Value
@Builder(toBuilder = true)
public class ItemWithTimeAndCommentDto {
    Long id;
    String name;
    String description;
    Boolean available;
    BookingIdOutDto lastBooking;
    BookingIdOutDto nextBooking;
    List<CommentDto> comments;
}
