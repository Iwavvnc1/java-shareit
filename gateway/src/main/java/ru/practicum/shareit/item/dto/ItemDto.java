package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Value
@Builder(toBuilder = true)
public class ItemDto {
    Long id;
    @NotBlank
    String name;
    @NotNull
    String description;
    @NotNull
    Boolean available;
    Long requestId;
}
