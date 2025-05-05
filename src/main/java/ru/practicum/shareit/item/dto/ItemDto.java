package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id"})
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available = Boolean.FALSE;
    private Long ownerId;
    private Long requestId;
}
