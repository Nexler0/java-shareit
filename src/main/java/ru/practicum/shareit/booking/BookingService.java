package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    List<Booking> findAllBooking(Long userId, String status, int from, int size);

    List<Booking> findAllUserBooking(Long userId, String status, int from, int size);

    Booking findBookingById(Long userId, Long bookingId);

    Booking createBooking(Long userId, Booking booking);

    Booking setApproveStatusToBooking(Long userId, Long bookingId, Boolean approve);

}
