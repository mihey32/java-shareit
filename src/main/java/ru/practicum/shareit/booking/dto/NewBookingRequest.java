package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.enums.Statuses;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"id"})
public class NewBookingRequest {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    @NotNull(message = "У бронироуемой вещи должен быть ID")
    @Positive(message = "ID не может быть отрицательным")
    private Long itemId;
    @NotNull(message = "Статус должен быть указан")
    private Statuses status;
    @NotNull(message = "ID пользователя который хочет забронировать вещь не может быть пустым")
    @Positive(message = "ID пользователя который хочет забронировать вещь не может быть отрицательным числом")
    private Long bookerId;
}