package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingShort;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public BookingDtoOut toDto(Booking booking) {
        return BookingDtoOut.builder()
                .id(booking.getId())
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }

    public Booking toBooking(BookingDtoIn bookingDtoIn) {
        if (!itemRepository.existsItemById(bookingDtoIn.getItemId())
                || !userRepository.existsUserById(bookingDtoIn.getBookerId())){
            throw new NotFoundException("Предмет или пользователь не создан");
        }
        Booking booking = new Booking();
        booking.setId(bookingDtoIn.getId());
        booking.setStartDate(bookingDtoIn.getStart());
        booking.setEndDate(bookingDtoIn.getEnd());
        booking.setStatus(bookingDtoIn.getStatus());
        booking.setItem(itemRepository.getItemById(bookingDtoIn.getItemId()));
        booking.setBooker(userRepository.getUserById(bookingDtoIn.getBookerId()));
        return booking;
    }

    public static BookingShort toBookingShort(Booking booking){
        return new BookingShort(booking.getId(), booking.getBooker().getId());
    }
}
