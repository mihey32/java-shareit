package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.dto.UpdateRequest;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestServiceImpl itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto create(@Valid @RequestBody NewRequest itemRequest) {
        return itemRequestService.create(itemRequest);
    }

    @GetMapping("/{id}")
    public ItemRequestDto findItemRequest(@PathVariable("id") Long itemId) {
        return itemRequestService.findItemRequest(itemId);
    }

    @GetMapping
    public Collection<ItemRequestDto> findAll() {
        return itemRequestService.findAll();
    }

    @PutMapping
    public ItemRequestDto update(@Valid @RequestBody UpdateRequest newItemRequest) {
        return itemRequestService.update(newItemRequest);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") Long itemId) {
        return itemRequestService.delete(itemId);
    }
}