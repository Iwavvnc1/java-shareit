package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Value
@Builder(toBuilder = true)
public class BookingIdOutDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Status status;
    Long bookerId;
    Long itemId;
    String itemName;
}
