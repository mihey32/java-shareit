package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class UpdateItemRequest {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    @Positive(message = "ID владельца вещи не может быть отрицательным числом")
    private Long ownerId;
    @Positive(message = "ID запроса на создание вещи не может быть отрицательным числом")
    private Long requestId;

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
