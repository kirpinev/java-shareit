package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.constraints.Min;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
            @RequestParam(value = "from", required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Min(1) Integer size) {
        return bookingClient.getBookings(userId, state, from, size);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam("approved") Boolean approved,
            @PathVariable("bookingId") Long bookingId) {
        return bookingClient.approveByOwner(userId, bookingId, approved);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
            @Validated @RequestBody BookingInputDto requestDto) {
        return bookingClient.create(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(value = "state", defaultValue = "ALL") BookingState state,
            @RequestParam(value = "from", required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", required = false, defaultValue = "10") @Min(1) Integer size) {
        return bookingClient.getAllByOwner(userId, state, from, size);
    }
}
