package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.dto.UpdateRequest;

@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @Valid @RequestBody NewRequest itemRequest) {
        return itemRequestClient.addItemRequest(userId, itemRequest);
    }

    @GetMapping("/{request-id}")
    public ResponseEntity<Object> findItemRequest(@PathVariable("request-id") Long requestId) {
        return itemRequestClient.getItemRequest(requestId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByRequestorId(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return itemRequestClient.getItemRequests(null, requestorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllOfAnotherRequestors(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return itemRequestClient.getItemRequests("/all", requestorId);
    }

    @PutMapping
    public ResponseEntity<Object> updateItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @Valid @RequestBody UpdateRequest newItemRequest) {
        return itemRequestClient.updateItemRequest(userId, newItemRequest);
    }

    @DeleteMapping("/{request-id}")
    public ResponseEntity<Object> deleteItemRequest(@PathVariable("request-id") Long requestId) {
        return itemRequestClient.deleteItemRequest(requestId);
    }
}
