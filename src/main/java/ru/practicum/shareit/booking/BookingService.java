package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    List<Booking> findAllBooking(Long userId, String status, Integer from, Integer size);

    List<Booking> findAllUserBooking(Long userId, String status, Integer from, Integer size);

    Booking findBookingById(Long userId, Long bookingId);

    Booking createBooking(Long userId, Booking booking);

    Booking setApproveStatusToBooking(Long userId, Long bookingId, Boolean approve);

}
