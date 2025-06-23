package ru.practicum.shareit.item.comment.dto;

import lombok.Data;

@Data
public class UpdateCommentRequest {
    private Long id;
    private String text;
}
