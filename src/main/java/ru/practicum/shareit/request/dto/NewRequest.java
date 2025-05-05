package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"id"})
public class NewRequest {
    private Long id;
    @NotBlank(message = "Запрос не должен быть пустым")
    private String description;
    @NotNull(message = "ID пользователя составившего заявку не может не может быть пустым")
    @Positive(message = "ID пользователя составившего заявку не может не может быть отрицательным числом")
    private Long requestorId;
    private LocalDateTime created;
}
