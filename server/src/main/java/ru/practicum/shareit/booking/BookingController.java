package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @GetMapping
    public List<BookingDtoOut> getAllBooking(@RequestParam(name = "state", defaultValue = "ALL") String status,
                                             @RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam(name = "from", defaultValue = "0",
                                                     required = false) Integer from,
                                             @RequestParam(name = "size", defaultValue = "10",
                                                     required = false) Integer size) {
        return bookingService.findAllBooking(userId, status, from, size).stream().map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getAllOwnerBooking(@RequestParam(name = "state", defaultValue = "ALL") String status,
                                                  @RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(name = "from", defaultValue = "0",
                                                          required = false) Integer from,
                                                  @RequestParam(name = "size", defaultValue = "10",
                                                          required = false) Integer size) {
        return bookingService.findAllUserBooking(userId, status, from, size).stream().map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PathVariable Long bookingId) {
        return bookingMapper.toDto(bookingService.findBookingById(userId, bookingId));
    }

    @PostMapping
    public BookingDtoOut createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestBody BookingDtoIn bookingIn) {
        bookingIn.setBookerId(userId);
        Booking booking = bookingMapper.toBooking(bookingIn);
        return bookingMapper.toDto(bookingService.createBooking(userId, booking));
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut setApproveBookingByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(name = "approved") Boolean approve,
                                                  @PathVariable Long bookingId) {
        return bookingMapper.toDto(bookingService.setApproveStatusToBooking(userId, bookingId, approve));
    }
}