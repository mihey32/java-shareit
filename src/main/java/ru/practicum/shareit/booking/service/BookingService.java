package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;

import java.util.Collection;

public interface BookingService {
    BookingDto create(Long userId, NewBookingRequest request);

    BookingDto findBooking(Long bookingId, Long userId);

    BookingDto update(UpdateBookingRequest updateRequest);

    void delete(Long bookingId);

    Collection<BookingDto> findAllBookingsByUser(Long userId, String state);

    Collection<BookingDto> findAllBookingsByOwnerItems(Long userId, String state);

    BookingDto approveBooking(Long bookingId, Long userId, Boolean approved);
}
