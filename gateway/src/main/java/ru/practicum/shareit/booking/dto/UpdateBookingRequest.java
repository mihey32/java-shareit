package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.enums.Statuses;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"id"})
public class UpdateBookingRequest {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Statuses status;
    private Long bookerId;

    public boolean hasStart() {
        return this.start != null;
    }

    public boolean hasEnd() {
        return this.end != null;
    }
}
