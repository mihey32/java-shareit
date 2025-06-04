package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.enums.States;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@GetMapping("/{booking-id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> findBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
											  @PathVariable("booking-id") Long bookingId) {
		return bookingClient.getBooking(userId, bookingId);
	}

	@PostMapping
	public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") Long userId,
										   @Valid @RequestBody NewBookingRequest requestDto) {
		return bookingClient.bookItem(userId, requestDto);
	}

	@GetMapping
	public ResponseEntity<Object> findAllBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
														@RequestParam(name = "state", defaultValue = "ALL") String stateParam) {
		States state = States.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		return bookingClient.getBookings(null, userId, state);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> findAllBookingsByOwnerItems(@RequestHeader("X-Sharer-User-Id") Long userId,
															  @RequestParam(name = "state", defaultValue = "ALL") String stateParam) {
		States state = States.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		return bookingClient.getBookings("/owner", userId, state);
	}

	@PutMapping
	public ResponseEntity<Object> updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
												@RequestBody UpdateBookingRequest newBooking) {
		return bookingClient.updateBooking(userId, newBooking);
	}

	@DeleteMapping("/{booking-id}")
	public ResponseEntity<Object> deleteBooking(@PathVariable("booking-id") Long bookingId) {
		return bookingClient.deleteBooking(bookingId);
	}

	@PatchMapping("/{booking-id}")
	public ResponseEntity<Object> approveBooking(@PathVariable("booking-id") Long bookingId,
												 @RequestHeader("X-Sharer-User-Id") Long userId,
												 @RequestParam(name = "approved", defaultValue = "false") Boolean approved) {
		return bookingClient.approveBooking(bookingId, userId, approved);
	}
}