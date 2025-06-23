package ru.practicum.shareit.item.service;

import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.enums.Statuses;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotItemOwnerException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.NewCommentRequest;
import ru.practicum.shareit.item.dto.AdvancedItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository repository,
                           UserRepository userRepository,
                           BookingRepository bookingRepository, CommentRepository commentRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public ItemDto create(Long ownerId, NewItemRequest request) {
        User findUser = findUserById(ownerId);

        Item item = ItemMapper.mapToItem(findUser, request);
        item = repository.save(item);

        return ItemMapper.mapToItemDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public AdvancedItemDto findItem(Long ownerId, Long itemId) {
        Item item = findById(itemId);

        LocalDateTime now = LocalDateTime.now();

        if (item.getUser().getId().equals(ownerId)) {
            return ItemMapper.mapToAdvancedItemDto(findById(itemId),
                    commentRepository.findAllByItemId(itemId),
                    getLastBookingEndDate(itemId, now),
                    getNextBookingStartDate(itemId, now));
        }

        return ItemMapper.mapToAdvancedItemDto(findById(itemId), commentRepository.findAllByItemId(itemId));
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemDto> findItemsForTenant(Long ownerId, String text) {
        if (StringUtils.isBlank(text)) {
            return new ArrayList<>();
        }

        return repository.findItemsForTenant(text.toLowerCase()).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<AdvancedItemDto> findAll(Long ownerId) {
        List<Item> userItems = repository.findAllByUserId(ownerId);

        if (!userItems.isEmpty()) {
            return fillItemData(userItems);
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public ItemDto update(Long itemId, UpdateItemRequest request, Long ownerId) {
        Item item = findById(itemId);

        if (!item.getUser().getId().equals(ownerId)) {
            throw new NotItemOwnerException("Редактировать данные вещи может только её владелец");
        }

        Item updatedItem = repository.save(ItemMapper.updateItemFields(item, request));

        return ItemMapper.mapToItemDto(updatedItem);
    }

    @Override
    @Transactional
    public void delete(Long ownerId, Long itemId) {
        Item item = findById(itemId);
        User findUser = findUserById(ownerId);

        if (!findUser.getId().equals(ownerId)) {
            throw new NotItemOwnerException("Удалить данные вещи может только её владелец");
        }
        repository.delete(item);
    }

    @Override
    @Transactional
    public CommentDto addComment(Long itemId, Long userId, NewCommentRequest commentRequest) {
        User findUser = findUserById(userId);
        Item findItem = findById(itemId);

        if (!bookingRepository.existsByBookerIdAndItemIdAndEndBefore(userId, itemId, LocalDateTime.now())) {
            throw new ValidationException(String.format("Пользователь %s не может оставить комментарий, " +
                    "так как не пользовался вещью %s", findUser.getName(), findItem.getName()));
        }

        Comment comment = CommentMapper.mapToComment(findUser, findItem, commentRequest);
        comment = commentRepository.save(comment);

        return CommentMapper.mapToCommentDto(comment);
    }

    private Item findById(Long itemId) {
        return repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь c ID " + itemId + " не найдена"));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Владелец вещи c ID " + userId + " не найден"));
    }

    private Optional<LocalDateTime> getLastBookingEndDate(Long itemId, LocalDateTime now) {
        return bookingRepository.findLastBookingEndByItemId(itemId, Statuses.APPROVED, now)
                .stream()
                .max(Comparator.naturalOrder());
    }

    private Optional<LocalDateTime> getNextBookingStartDate(Long itemId, LocalDateTime now) {
        return bookingRepository.findNextBookingStartByItemId(itemId, Statuses.APPROVED, now)
                .stream()
                .min(Comparator.naturalOrder());
    }

    private List<AdvancedItemDto> fillItemData(List<Item> userItems) {
        LocalDateTime now = LocalDateTime.now();
        List<Long> itemIds = userItems.stream().map(Item::getId).toList();

        Map<Item, LocalDateTime> lastItemBookingEndDate = bookingRepository
                .findByItemInAndEndBefore(itemIds, Statuses.APPROVED, now)
                .stream()
                .collect(Collectors.toMap(Booking::getItem, Booking::getEnd));

        Map<Item, LocalDateTime> nextItemBookingStartDate = bookingRepository
                .findByItemInAndStartAfter(itemIds, Statuses.APPROVED, now)
                .stream()
                .collect(Collectors.toMap(Booking::getItem, Booking::getStart));

        Map<Item, List<Comment>> itemsWithComments = commentRepository
                .findByItemIn(itemIds)
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));

        List<AdvancedItemDto> itemsList = new ArrayList<>();
        for (Item item : userItems) {
            Optional<LocalDateTime> lastEndDate;
            if (!lastItemBookingEndDate.isEmpty()) {
                lastEndDate = Optional.of(lastItemBookingEndDate.get(item));
            } else {
                lastEndDate = Optional.empty();
            }

            Optional<LocalDateTime> nextStartDate;
            if (!nextItemBookingStartDate.isEmpty()) {
                nextStartDate = Optional.of(nextItemBookingStartDate.get(item));
            } else {
                nextStartDate = Optional.empty();
            }

            itemsList.add(ItemMapper.mapToAdvancedItemDto(item,
                            itemsWithComments.getOrDefault(item, Collections.emptyList()),
                            lastEndDate,
                            nextStartDate
                    )
            );
        }
        return itemsList;
    }
}