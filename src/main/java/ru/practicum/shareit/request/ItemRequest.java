package ru.practicum.shareit.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private Long id;
    @NotBlank(message = "Запрос не должен быть пустым")
    private String description;
    @NotNull(message = "ID пользователя составившего заявку не может не может быть пустым")
    @Positive(message = "ID пользователя составившего заявку не может не может быть отрицательным числом")
    private Long requestorId;
    private LocalDateTime created;
}