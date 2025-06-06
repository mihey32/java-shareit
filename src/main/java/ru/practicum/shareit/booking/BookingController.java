package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.booking.service.BookingServiceImpl;

import java.util.Collection;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingServiceImpl bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                             @Valid @RequestBody NewBookingRequest booking) {
        return bookingService.create(userId, booking);
    }

    @GetMapping("/{booking-id}")
    public BookingDto findBooking(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                  @PathVariable("booking-id") @Positive Long bookingId) {
        return bookingService.findBooking(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDto> findAllBookingsByUser(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                        @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.findAllBookingsByUser(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> findAllBookingsByOwnerItems(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                              @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.findAllBookingsByOwnerItems(userId, state);
    }

    @PutMapping("/{booking-id}")
    public BookingDto update(@Valid @RequestBody UpdateBookingRequest newItem) {
        return bookingService.update(newItem);
    }

    @DeleteMapping("/{booking-id}")
    public void delete(@PathVariable("booking-id") @Positive Long bookingId) {
        bookingService.delete(bookingId);
    }

    @PatchMapping("/{booking-id}")
    public BookingDto approveBooking(@PathVariable("booking-id") @Positive Long bookingId,
                                     @RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                     @RequestParam(name = "approved", defaultValue = "false") Boolean approved) {
        return bookingService.approveBooking(bookingId, userId, approved);
    }
}