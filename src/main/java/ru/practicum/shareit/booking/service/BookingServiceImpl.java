package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.storage.BookingStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    BookingStorage bookingStorage;

    @Autowired
    public BookingServiceImpl(BookingStorage bookingStorage) {
        this.bookingStorage = bookingStorage;
    }

    @Override
    public BookingDto create(NewBookingRequest request) {
        Booking booking = BookingMapper.mapToBooking(request);
        booking = bookingStorage.create(booking);

        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public BookingDto findBooking(Long bookingId) {
        return BookingMapper.mapToBookingDto(bookingStorage.findBooking(bookingId));
    }

    @Override
    public Collection<BookingDto> findAll() {
        return bookingStorage.getBookings().stream().map(BookingMapper::mapToBookingDto).collect(Collectors.toList());
    }

    @Override
    public BookingDto update(UpdateBookingRequest updateRequest) {
        Booking updatedItem = BookingMapper.updateBookingFields(bookingStorage.findBooking(updateRequest.getId()), updateRequest);
        updatedItem = bookingStorage.update(updatedItem);

        return BookingMapper.mapToBookingDto(updatedItem);
    }

    @Override
    public boolean delete(Long bookingId) {
        return bookingStorage.delete(bookingId);
    }
}
