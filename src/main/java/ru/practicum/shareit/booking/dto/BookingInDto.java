package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class BookingInDto {

    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Long bookerId;
    Long itemId;

}
