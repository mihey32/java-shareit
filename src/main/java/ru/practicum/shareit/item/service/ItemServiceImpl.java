package ru.practicum.shareit.item.service;

import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotItemOwnerException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemServiceImpl(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    @Override
    public ItemDto create(Long ownerId, NewItemRequest request) {
        User user = userStorage.findUser(ownerId);

        Item item = ItemMapper.mapToItem(ownerId, request);
        item = itemStorage.create(item);

        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto findItem(Long ownerId, Long itemId) {
        return ItemMapper.mapToItemDto(itemStorage.findItem(itemId));
    }

    @Override
    public Collection<ItemDto> findItemsForTenant(Long ownerId, String text) {
        if (StringUtils.isBlank(text)) {
            return new ArrayList<>();
        }

        return itemStorage.findItemsForTenant(text.toLowerCase()).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> findAll(Long ownerId) {
        return itemStorage.getItems(ownerId).stream().map(ItemMapper::mapToItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto update(Long itemId, UpdateItemRequest request, Long ownerId) {
        Item item = itemStorage.findItem(itemId);

        if (!item.getOwnerId().equals(ownerId)) {
            throw new NotItemOwnerException("Редактировать данные вещи может только её владелец");
        }

        Item updatedItem = itemStorage.update(ItemMapper.updateItemFields(item, request));

        return ItemMapper.mapToItemDto(updatedItem);
    }

    @Override
    public boolean delete(Long ownerId, Long itemId) {
        Item item = itemStorage.findItem(itemId);
        return itemStorage.delete(itemId);
    }
}
