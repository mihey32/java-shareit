package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewItemRequest {
    @NotBlank(message = "Название вещи не должно быть пустым")
    private String name;
    @NotBlank(message = "Описание вещи не должно быть пустым")
    private String description;
    @NotNull(message = "Статус вещи не может быть пустым.")
    private Boolean available;
    @Positive(message = "ID владельца вещи не может быть отрицательным числом")
    private Long ownerId;
    @Positive(message = "ID запроса на создание вещи не может быть отрицательным числом")
    private Long requestId;
    public boolean hasRequestId() {
        return requestId != null;
    }
}
