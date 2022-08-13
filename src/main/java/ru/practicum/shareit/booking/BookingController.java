package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingMapper;

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
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.findAllBooking(userId, status).stream().map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getAllOwnerBooking(@RequestParam(name = "state", defaultValue = "ALL") String status,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.findAllUserBooking(userId, status).stream().map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PathVariable Long bookingId) {
        return bookingMapper.toDto(bookingService.findBookingById(userId, bookingId));
    }

    @PostMapping
    public BookingDtoOut createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @RequestBody BookingDtoIn booking) {
        return bookingMapper.toDto(bookingService.createBooking(userId, bookingMapper.toBooking(booking)));
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut approveBookingByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(name = "approved") Boolean approve,
                                              @PathVariable Long bookingId) {
        return bookingMapper.toDto(bookingService.approveBookingByOwner(userId, bookingId, approve));
    }
}
