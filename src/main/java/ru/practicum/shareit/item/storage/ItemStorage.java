package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {
    Item create(Item item);

    Item findItem(Long itemId);

    Collection<Item> findItemsForTenant(String text);

    Collection<Item> getItems(Long ownerId);

    Item update(Item updateItem);

    boolean delete(Long itemId);
}
