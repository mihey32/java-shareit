package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id"})
public class Item {
    private Long id;
    @NotBlank(message = "Название не должно быть пустым")
    private String name;
    @NotBlank(message = "Описание не должно быть пустым")
    private String description;
    @NotNull(message = "Статус должен быть указан")
    private Boolean available;
    @Positive(message = "ID владельца вещи не может быть отрицательным числом")
    private Long ownerId;
    @Positive(message = "ID запроса на создание вещи не может быть отрицательным числом")
    Long requestId;
}