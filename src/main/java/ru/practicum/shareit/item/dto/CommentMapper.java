package ru.practicum.shareit.item.dto;

import lombok.Value;
import ru.practicum.shareit.item.model.Comment;

@Value
public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}
