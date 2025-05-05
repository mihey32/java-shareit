package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"id"})
public class UpdateRequest {
    private Long id;
    private String description;
    private Long requestorId;
    private LocalDateTime created;
}
