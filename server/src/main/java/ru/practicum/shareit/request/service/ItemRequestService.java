package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.dto.UpdateRequest;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto create(Long userId, NewRequest request);

    ItemRequestDto findItemRequest(Long itemRequestId);

    //Collection<ItemRequestDto> findAll();

    ItemRequestDto update(Long userId, UpdateRequest request);

    void delete(Long itemRequestId);

    Collection<ItemRequestDto> findAllByRequestorId(Long requestorId);

    Collection<ItemRequestDto> findAllOfAnotherRequestors(Long requestorId);
}