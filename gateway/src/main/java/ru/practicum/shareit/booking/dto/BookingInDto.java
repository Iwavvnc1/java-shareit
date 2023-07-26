package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class BookingInDto {

    private Long id;
    @NotNull
    @Future
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    private Long bookerId;
    @NotNull
    private Long itemId;
}
