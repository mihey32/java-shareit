package ru.practicum.shareit.booking.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryBookingStorage implements BookingStorage {
    Map<Long, Booking> bookings = new HashMap<>();

    @Override
    public Booking create(Booking booking) {
        booking.setId(getNextId());
        bookings.put(booking.getId(), booking);
        return booking;
    }

    @Override
    public Booking findBooking(Long bookingId) {
        return Optional.ofNullable(bookings.get(bookingId))
                .orElseThrow(() -> new NotFoundException(String.format("Бронирование c ID %d не найдено", bookingId)));
    }

    @Override
    public Collection<Booking> getBookings() {
        return bookings.values();
    }

    @Override
    public Booking update(Booking updateBooking) {
        bookings.put(updateBooking.getId(), updateBooking);
        return updateBooking;
    }

    @Override
    public boolean delete(Long bookingId) {
        bookings.remove(bookingId);
        return Optional.ofNullable(bookings.get(bookingId)).isPresent();
    }

    private long getNextId() {
        long currentMaxId = bookings.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
