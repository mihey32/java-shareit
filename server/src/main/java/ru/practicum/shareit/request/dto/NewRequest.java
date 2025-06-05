package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewRequest {
    @NotBlank(message = "Описание запроса не может быть пустым")
    private String description;
    @Positive(message = "ID пользователя составившего заявку не может  быть отрицательным числом")
    private Long requestorId;
}
