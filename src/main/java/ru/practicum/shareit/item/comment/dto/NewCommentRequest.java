package ru.practicum.shareit.item.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class NewCommentRequest {
    @NotBlank(message = "Комментарий не может быть пустым")
    private String text;
    @Positive(message = "ID предмета, на который пишут комментарий, не может быть отрицательным числом")
    private Long itemId;
    @Positive(message = "ID пользователя, который пишет комментарий, не может быть отрицательным числом")
    private Long authorId;
}
