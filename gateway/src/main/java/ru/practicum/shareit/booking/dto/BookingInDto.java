package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class BookingInDto {

    Long id;
    @NotNull
    @Future
    LocalDateTime start;
    @NotNull
    LocalDateTime end;
    Long bookerId;
    @NotBlank
    Long itemId;
}
