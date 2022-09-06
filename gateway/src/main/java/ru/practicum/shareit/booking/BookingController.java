package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero
                                              @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive
                                              @RequestParam(name = "size", defaultValue = "10") Integer size) {
//        BookingState state = BookingState.from(stateParam)
//                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, stateParam, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsAllOwner(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                      @PositiveOrZero
                                                      @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                      @Positive
                                                      @RequestParam(name = "size", defaultValue = "10") Integer size) {
//        BookingState state = BookingState.from(stateParam)
//                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("getBookingsAllOwner with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookingsByOwner(userId, stateParam, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody @Valid BookingDtoIn bookingDtoIn) {
        log.info("Creating booking {}, userId={}", bookingDtoIn.toString(), userId);
        return bookingClient.addBooking(userId, bookingDtoIn);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> setApproveBookingByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                           @RequestParam(name = "approved") Boolean approve,
                                                           @PathVariable Long bookingId) {
        log.info("setApproveBookingByOwner bookingId={}, userId={}", bookingId, userId);
        return bookingClient.setApproveBookingByOwner(userId, bookingId, approve);
    }
}