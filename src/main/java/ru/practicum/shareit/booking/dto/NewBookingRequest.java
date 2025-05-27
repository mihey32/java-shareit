package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewBookingRequest {
    @FutureOrPresent(message = "Нельзя начать бронирование раньше текущего дня!")
    private LocalDateTime start;
    @Future(message = "Нельзя закончить бронирование в этот же день или раньше!")
    private LocalDateTime end;
    @NotNull(message = "У бронироуемой вещи должен быть ID")
    @Positive(message = "ID не может быть отрицательным")
    private Long itemId;
    @Positive(message = "ID пользователя который хочет забронировать вещь не может быть отрицательным числом")
    private Long bookerId;
}