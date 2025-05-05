package ru.practicum.shareit.request.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.ItemRequest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryItemRequestStorage implements ItemRequestStorage {
    Map<Long, ItemRequest> itemRequests = new HashMap<>();

    @Override
    public ItemRequest create(ItemRequest request) {
        request.setId(getNextId());
        //log.trace("Данные о запросе с ID {} сохранены!", request.getId());
        itemRequests.put(request.getId(), request);
        return request;
    }

    @Override
    public ItemRequest findItemRequest(Long itemRequestId) {
        return Optional.ofNullable(itemRequests.get(itemRequestId))
                .orElseThrow(() -> new NotFoundException(String.format("Запрос c ID %d не найден", itemRequestId)));
    }

    @Override
    public Collection<ItemRequest> getItemRequests() {
        return itemRequests.values();
    }

    @Override
    public ItemRequest update(ItemRequest updateRequest) {
        itemRequests.put(updateRequest.getId(), updateRequest);
        return updateRequest;
    }

    @Override
    public boolean delete(Long itemRequestId) {
        itemRequests.remove(itemRequestId);
        return Optional.ofNullable(itemRequests.get(itemRequestId)).isPresent();
    }

    private long getNextId() {
        long currentMaxId = itemRequests.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
