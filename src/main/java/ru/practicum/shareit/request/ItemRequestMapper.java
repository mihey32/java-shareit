package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.dto.UpdateRequest;

public class ItemRequestMapper {
    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setRequestorId(itemRequest.getRequestorId());
        dto.setCreated(itemRequest.getCreated());

        return dto;
    }

    public static ItemRequest mapToItemRequest(NewRequest request) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(request.getDescription());
        itemRequest.setRequestorId(request.getRequestorId());
        itemRequest.setCreated(request.getCreated());

        return itemRequest;
    }

    public static ItemRequest updateItemFields(ItemRequest itemRequest, UpdateRequest request) {
        itemRequest.setDescription(request.getDescription());

        return itemRequest;
    }
}
