package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id"})
public class UpdateItemRequest {
    Long id;
    String name;
    String description;
    Boolean available;
    @Positive(message = "ID владельца вещи не может быть отрицательным числом")
    Long ownerId;
    @Positive(message = "ID запроса на создание вещи не может быть отрицательным числом")
    Long requestId;

    public boolean hasName() {

        return name != null && !name.trim().isEmpty();
    }

    public boolean hasDescription() {
        return description != null && !description.trim().isEmpty();
    }

    public boolean hasAvailable() {
        return available != null;
    }
}
