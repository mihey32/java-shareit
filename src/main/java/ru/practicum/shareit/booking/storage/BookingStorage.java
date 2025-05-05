package ru.practicum.shareit.booking.storage;

import ru.practicum.shareit.booking.Booking;

import java.util.Collection;

public interface BookingStorage {
    Booking create(Booking booking);

    Booking findBooking(Long bookingId);

    Collection<Booking> getBookings();

    Booking update(Booking updateBooking);

    boolean delete(Long bookingId);
}
