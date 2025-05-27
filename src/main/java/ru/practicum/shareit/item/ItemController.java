package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.NewCommentRequest;
import ru.practicum.shareit.item.dto.AdvancedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @Valid @RequestBody NewItemRequest item) {
        return itemService.create(userId, item);
    }

    @GetMapping("/{item-id}")
    public AdvancedItemDto findItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                    @PathVariable("item-id") Long itemId) {
        return itemService.findItem(ownerId, itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> findItemsForTenant(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                  @RequestParam(name = "text", defaultValue = "") String text) {
        return itemService.findItemsForTenant(ownerId, text);
    }

    @GetMapping
    public Collection<AdvancedItemDto> findAll(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.findAll(ownerId);
    }

    @PatchMapping("/{item-id}")
    public ItemDto update(@PathVariable("item-id") Long itemId,
                          @Valid @RequestBody UpdateItemRequest newItem,
                          @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.update(itemId, newItem, ownerId);
    }

    @DeleteMapping("/{item-id}")
    public void delete(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                       @PathVariable("item-id") Long itemId) {
        itemService.delete(ownerId, itemId);
    }

    @PostMapping("/{item-id}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable("item-id") Long itemId,
                                 @RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody NewCommentRequest comment) {
        return itemService.addComment(itemId, userId, comment);
    }
}