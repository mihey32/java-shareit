package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.dto.ResponseDto;
import ru.practicum.shareit.request.dto.UpdateRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

public class ItemRequestMapper {
    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setRequestorId(itemRequest.getRequestor().getId());
        dto.setCreated(itemRequest.getCreated());
        dto.setItems(Collections.emptyList());

        return dto;
    }

    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest, Collection<Item> items) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(itemRequest.getId());
        dto.setDescription(itemRequest.getDescription());
        dto.setRequestorId(itemRequest.getRequestor().getId());
        dto.setCreated(itemRequest.getCreated());
        dto.setItems(items.stream().map(ItemRequestMapper::mapToResponseDto).toList());

        return dto;
    }

    public static ItemRequest mapToItemRequest(NewRequest request, User findUser, LocalDateTime now) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(request.getDescription());
        itemRequest.setRequestor(findUser);
        itemRequest.setCreated(now);

        return itemRequest;
    }

    public static ItemRequest updateItemFields(ItemRequest itemRequest, UpdateRequest request, User findUser) {
        itemRequest.setDescription(request.getDescription());

        return itemRequest;
    }

    private static ResponseDto mapToResponseDto(Item item) {
        ResponseDto dto = new ResponseDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setOwnerId(item.getUser().getId());

        return dto;
    }
}