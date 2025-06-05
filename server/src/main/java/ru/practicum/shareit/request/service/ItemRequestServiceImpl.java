package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequest;
import ru.practicum.shareit.request.dto.UpdateRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final RequestRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public ItemRequestServiceImpl(RequestRepository requestRepository,
                                  UserRepository userRepository,
                                  ItemRepository itemRepository)  {
        this.repository = requestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public ItemRequestDto create(Long userId, NewRequest request) {
        User findUser = findUserById(userId);

        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(request, findUser, LocalDateTime.now());
        itemRequest = repository.save(itemRequest);

        return ItemRequestMapper.mapToItemRequestDto(itemRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDto findItemRequest(Long itemRequestId) {
        ItemRequest itemRequest = findById(itemRequestId);

        Collection<Item> items = itemRepository.findByRequestId(itemRequestId);

        return ItemRequestMapper.mapToItemRequestDto(itemRequest, items);
    }

    @Override
    @Transactional
    public ItemRequestDto update(Long userId, UpdateRequest updateRequest) {
        User findUser = findUserById(userId);

        ItemRequest updatedItem = ItemRequestMapper.updateItemFields(findById(updateRequest.getId()), updateRequest, findUser);
        updatedItem = repository.save(updatedItem);

        return ItemRequestMapper.mapToItemRequestDto(updatedItem);
    }

    @Override
    @Transactional
    public void delete(Long itemRequestId) {
        ItemRequest itemRequest = findById(itemRequestId);
        repository.delete(itemRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemRequestDto> findAllByRequestorId(Long requestorId) {
        User findUser = findUserById(requestorId);

        List<ItemRequest> requests = repository.findByRequestorId(requestorId);

        return fillRequestsData(requests)
                .stream()
                .sorted(Comparator.comparing(ItemRequestDto::getCreated).reversed())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemRequestDto> findAllOfAnotherRequestors(Long requestorId) {
        return repository.findByRequestorIdNotOrderByCreatedDesc(requestorId)
                .stream()
                .map(ItemRequestMapper::mapToItemRequestDto)
                .collect(Collectors.toList());
    }

    private ItemRequest findById(Long itemRequestId) {
        return repository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException("Запрос c ID " + itemRequestId + " не найден"));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Владелец вещи c ID " + userId + " не найден"));
    }

    private List<ItemRequestDto> fillRequestsData(List<ItemRequest> requests) {

        List<Long> requestIds = requests.stream()
                .map(ItemRequest::getId)
                .toList();

        Map<Long, List<Item>> requestItems = itemRepository
                .findByRequestIdIn(requestIds)
                .stream()
                .collect(groupingBy(Item::getRequestId, toList()));

        List<ItemRequestDto> requestsList = new ArrayList<>();
        for (ItemRequest request : requests) {

            requestsList.add(ItemRequestMapper.mapToItemRequestDto(request,
                    requestItems.getOrDefault(request.getId(), Collections.emptyList()))
            );
        }

        return requestsList;
    }
}