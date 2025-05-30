package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.dto.UpdateRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestServiceImpl itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                 @Valid @RequestBody NewRequest itemRequest) {
        return itemRequestService.create(userId, itemRequest);
    }

    @GetMapping("/{request-id}")
    public ItemRequestDto findItemRequest(@PathVariable("request-id") @Positive Long itemId) {
        return itemRequestService.findItemRequest(itemId);
    }

    @GetMapping
    public Collection<ItemRequestDto> findAll() {
        return itemRequestService.findAll();
    }

    @PutMapping("/{request-id}")
    public ItemRequestDto update(@PathVariable("request-id") @Positive Long requestId,
                                 @Valid @RequestBody UpdateRequest newItemRequest) {
        return itemRequestService.update(requestId, newItemRequest);
    }

    @DeleteMapping("/{request-id}")
    public void delete(@PathVariable("request-id") @Positive Long itemId) {
        itemRequestService.delete(itemId);
    }
}