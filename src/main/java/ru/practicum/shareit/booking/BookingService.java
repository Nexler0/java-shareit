package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    List<Booking> findAllBooking(Long userId, String status);

    List<Booking> findAllUserBooking(Long userId, String status);

    Booking findBookingById(Long userId, Long bookingId);

    Booking createBooking(Long userId, Booking booking);

    Booking approveBookingByOwner(Long userId, Long bookingId, Boolean approve);

}
