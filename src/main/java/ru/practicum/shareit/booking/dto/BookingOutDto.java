package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class BookingOutDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Status status;
    User booker;
    Item item;

}
