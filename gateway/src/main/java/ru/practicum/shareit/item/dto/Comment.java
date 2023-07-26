package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class Comment {
    private Long id;
    @NotBlank
    private String text;
    private String authorName;
    private LocalDateTime created;
}
