package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.dto.UpdateRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @RequestBody NewRequest itemRequest) {
        return itemRequestService.create(userId, itemRequest);
    }

    @GetMapping("/{request-id}")
    public ItemRequestDto findItemRequest(@PathVariable("request-id") Long itemId) {
        return itemRequestService.findItemRequest(itemId);
    }

    @GetMapping
    public Collection<ItemRequestDto> findAllByRequestorId(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return itemRequestService.findAllByRequestorId(requestorId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> findAllOfAnotherRequestors(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return itemRequestService.findAllOfAnotherRequestors(requestorId);
    }

    @PutMapping
    public ItemRequestDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @RequestBody UpdateRequest newItemRequest) {
        return itemRequestService.update(userId, newItemRequest);
    }

    @DeleteMapping("/{request-id}")
    public void delete(@PathVariable("request-id") Long itemId) {
        itemRequestService.delete(itemId);
    }
}