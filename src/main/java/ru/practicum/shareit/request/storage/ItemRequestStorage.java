package ru.practicum.shareit.request.storage;

import ru.practicum.shareit.request.ItemRequest;

import java.util.Collection;

public interface ItemRequestStorage {
    ItemRequest create(ItemRequest request);

    ItemRequest findItemRequest(Long itemRequestId);

    Collection<ItemRequest> getItemRequests();

    ItemRequest update(ItemRequest updateRequest);

    boolean delete(Long itemRequestId);
}
