package ru.practicum.shareit.booking;

import org.springframework.context.annotation.Lazy;

public class BookingShortRepositoryImpl implements BookingShortRepositoryCustom{
    private final BookingShortRepository bookingShortRepository;

    public BookingShortRepositoryImpl(@Lazy BookingShortRepository bookingShortRepository){
        this.bookingShortRepository = bookingShortRepository;
    }
}
