package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.NewCommentRequest;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                          @Valid @RequestBody NewItemRequest item) {
        return itemClient.addItem(ownerId, item);
    }

    @GetMapping("/{item-id}")
    public ResponseEntity<Object> findItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                           @PathVariable("item-id") Long itemId) {
        return itemClient.getItem(ownerId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItemsForTenant(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                     @RequestParam(name = "text", defaultValue = "") String text) {
        return itemClient.getItems("/search", ownerId, text);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemClient.getItems(null, ownerId, null);
    }

    @PatchMapping("/{item-id}")
    public ResponseEntity<Object> updateItem(@PathVariable("item-id") Long itemId,
                                             @Valid @RequestBody UpdateItemRequest newItem,
                                             @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemClient.updateItem(itemId, ownerId, newItem);
    }

    @DeleteMapping("/{item-id}")
    public ResponseEntity<Object> deleteItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                             @PathVariable("item-id") Long itemId) {
        return itemClient.deleteItem(itemId, ownerId);
    }

    @PostMapping("/{item-id}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addComment(@PathVariable("item-id") Long itemId,
                                             @RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Valid @RequestBody NewCommentRequest comment) {
        return itemClient.addComment("/" + itemId + "/comment", userId, comment);
    }
}
