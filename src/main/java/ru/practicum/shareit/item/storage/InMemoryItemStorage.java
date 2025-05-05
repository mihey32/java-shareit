package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryItemStorage implements ItemStorage {
    Map<Long, Item> items = new HashMap<>();

    @Override
    public Item create(Item item) {
        item.setId(getNextId());
        //log.trace("Данные о вещи c ID {} сохранены!", item.getName());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item findItem(Long itemId) {
        return Optional.ofNullable(items.get(itemId))
                .orElseThrow(() -> new NotFoundException(String.format("Вещь c ID %d не найдена", itemId)));
    }

    @Override
    public Collection<Item> findItemsForTenant(String text) {
        return items.values()
                .stream()
                .filter(item -> checkItem(item, text))
                .toList();
    }

    @Override
    public Collection<Item> getItems(Long ownerId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwnerId().equals(ownerId))
                .toList();
    }

    @Override
    public Item update(Item updateItem) {
        items.put(updateItem.getId(), updateItem);
        return updateItem;
    }

    @Override
    public boolean delete(Long itemId) {
        items.remove(itemId);
        return Optional.ofNullable(items.get(itemId)).isPresent();
    }

    private long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private boolean checkItem(Item item, String text) {
        return item.getAvailable().equals(Boolean.TRUE)
                && (item.getName().toLowerCase().contains(text)
                || item.getDescription().toLowerCase().contains(text));
    }
}
