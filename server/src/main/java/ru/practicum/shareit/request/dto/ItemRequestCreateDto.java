package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class ItemRequestCreateDto {
    Long id;
    @NotNull
    String description;
    LocalDateTime created;
}