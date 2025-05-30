package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.enums.States;
import ru.practicum.shareit.enums.Statuses;
import ru.practicum.shareit.exception.BookingStatusException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotItemOwnerException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository repository, UserRepository userRepository, ItemRepository itemRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public BookingDto create(Long userId, NewBookingRequest request) {
        Item findItem = findItemById(request.getItemId());
        User findUser = findUserById(userId);

        if (!findItem.getAvailable()) {
            throw new ValidationException("Вещь не доступна для бронирования!");
        }

        if (findUser.getId().equals(findItem.getUser().getId())) {
            throw new ValidationException("Нельзя бронировать собственную вещь");
        }

        Booking booking = BookingMapper.mapToBooking(request, findUser, findItem);
        booking = repository.save(booking);

        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto findBooking(Long bookingId, Long userId) {
        Booking booking = findById(bookingId);
        User owner = findUserById(booking.getItem().getUser().getId());

        if (!booking.getBooker().getId().equals(userId) && !owner.getId().equals(userId)) {
            throw new ValidationException("Данные о бронировании могут просматривать только владелец вещи и создатель брони.");
        }

        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto update(UpdateBookingRequest updateRequest) {

        if (updateRequest.getId() == null) {
            throw new ValidationException("ID бронирования должен быть указан");
        }

        Booking updatedItem = BookingMapper.updateBookingFields(findById((updateRequest.getId())), updateRequest);
        updatedItem = repository.save(updatedItem);

        return BookingMapper.mapToBookingDto(updatedItem);
    }

    @Override
    @Transactional
    public void delete(Long bookingId) {
        Booking booking = findById(bookingId);
        repository.delete(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<BookingDto> findAllBookingsByUser(Long userId, String state) {
        States currentState = States.valueOf(state);
        findUserById(userId);
        Collection<Booking> bookingList = switch (currentState) {
            case ALL -> repository.findAllByBookerId(userId);
            case CURRENT -> repository.findAllCurrentBookingByBookerId(userId);
            case PAST -> repository.findAllPastBookingByBookerId(userId);
            case FUTURE -> repository.findAllFutureBookingByBookerId(userId);
            case WAITING -> repository.findAllByBookerIdAndStatus(userId, Statuses.WAITING);
            case REJECTED -> repository.findAllByBookerIdAndStatus(userId, Statuses.REJECTED);
        };

        return bookingList.stream()
                .map(BookingMapper::mapToBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<BookingDto> findAllBookingsByOwnerItems(Long userId, String state) {
        States currentState = States.valueOf(state);
        findUserById(userId);
        Collection<Booking> bookingList = switch (currentState) {
            case ALL -> repository.findAllByOwnerId(userId);
            case CURRENT -> repository.findAllCurrentBookingByOwnerId(userId);
            case PAST -> repository.findAllPastBookingByOwnerId(userId);
            case FUTURE -> repository.findAllFutureBookingByOwnerId(userId);
            case WAITING -> repository.findAllByOwnerIdAndStatus(userId, Statuses.WAITING);
            case REJECTED -> repository.findAllByOwnerIdAndStatus(userId, Statuses.REJECTED);
        };

        return bookingList.stream()
                .map(BookingMapper::mapToBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingDto approveBooking(Long bookingId, Long userId, Boolean approved) {
        Booking booking = findById(bookingId);
        Item item = findItemById(booking.getItem().getId());

        if (!item.getUser().getId().equals(userId)) {
            throw new NotItemOwnerException("Менять статус вещи может только её владелец");
        }

        if (!booking.getStatus().equals(Statuses.WAITING)) {
            throw new BookingStatusException("Вещь недоступна для бронирования");
        }

        booking.setStatus(approved ? Statuses.APPROVED : Statuses.REJECTED);
        return BookingMapper.mapToBookingDto(booking);
    }

    private Booking findById(Long bookingId) {
        return repository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование c ID " + bookingId + " не найдено"));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь, создающий бронирование, c ID " + userId + " не найден"));
    }

    private Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь для бронирования c ID " + itemId + " не найдена"));
    }
}
