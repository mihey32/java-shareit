package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.dto.UpdateRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final RequestRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public ItemRequestServiceImpl(RequestRepository requestRepository, UserRepository userRepository) {
        this.repository = requestRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ItemRequestDto create(Long userId, NewRequest request) {
        User findUser = findUserById(userId);

        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(request, findUser);
        itemRequest = repository.save(itemRequest);

        return ItemRequestMapper.mapToItemRequestDto(itemRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDto findItemRequest(Long itemRequestId) {
        return ItemRequestMapper.mapToItemRequestDto(findById(itemRequestId));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemRequestDto> findAll() {
        return repository.findAll()
                .stream()
                .map(ItemRequestMapper::mapToItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemRequestDto update(Long requestId, UpdateRequest updateRequest) {
        if (requestId == null) {
            throw new ValidationException("ID запроса должен быть указан");
        }

        ItemRequest updatedItem = ItemRequestMapper.updateItemFields(findById(requestId), updateRequest);
        updatedItem = repository.save(updatedItem);

        return ItemRequestMapper.mapToItemRequestDto(updatedItem);
    }

    @Override
    @Transactional
    public void delete(Long itemRequestId) {
        ItemRequest itemRequest = findById(itemRequestId);
        repository.delete(itemRequest);
    }

    private ItemRequest findById(Long itemRequestId) {
        return repository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException("Запрос c ID " + itemRequestId + " не найден"));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Владелец вещи c ID " + userId + " не найден"));
    }
}