package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class ItemRequestCreateDto {
    Long id;
    String description;
    LocalDateTime created;
}