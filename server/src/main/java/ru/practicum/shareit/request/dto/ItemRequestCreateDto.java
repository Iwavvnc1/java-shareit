package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class ItemRequestCreateDto {
    private Long id;
    @NotNull
    private String description;
    private LocalDateTime created;
}