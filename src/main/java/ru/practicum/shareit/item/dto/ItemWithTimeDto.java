package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.booking.dto.BookingIdOutDto;


@Value
@Builder(toBuilder = true)
public class ItemWithTimeDto {
    Long id;
    String name;
    String description;
    Boolean available;
    BookingIdOutDto lastBooking;
    BookingIdOutDto nextBooking;
}
