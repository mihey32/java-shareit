package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.dto.UpdateRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    ItemRequestStorage itemRequestStorage;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestStorage itemRequestStorage) {
        this.itemRequestStorage = itemRequestStorage;
    }

    @Override
    public ItemRequestDto create(NewRequest request) {
        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(request);
        itemRequest = itemRequestStorage.create(itemRequest);

        return ItemRequestMapper.mapToItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto findItemRequest(Long itemRequestId) {
        return ItemRequestMapper.mapToItemRequestDto(itemRequestStorage.findItemRequest(itemRequestId));
    }

    @Override
    public Collection<ItemRequestDto> findAll() {
        return itemRequestStorage.getItemRequests()
                .stream()
                .map(ItemRequestMapper::mapToItemRequestDto).collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto update(UpdateRequest updateRequest) {
        ItemRequest updatedItem = ItemRequestMapper
                        .updateItemFields(itemRequestStorage
                        .findItemRequest(updateRequest.getId()), updateRequest);
        updatedItem = itemRequestStorage.update(updatedItem);

        return ItemRequestMapper.mapToItemRequestDto(updatedItem);
    }

    @Override
    public boolean delete(Long itemRequestId) {
        return itemRequestStorage.delete(itemRequestId);
    }
}
