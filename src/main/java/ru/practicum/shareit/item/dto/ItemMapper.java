package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public ItemDtoOut toDto(Item item) {
        return ItemDtoOut.builder()
                .id(item.getId())
                .userId(item.getUser().getId())
                .requestId(null)
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(item.getLastBooking())
                .nextBooking(item.getNextBooking())
                .build();
    }

    public Item toItem(ItemDtoIn itemDtoIn) {
        Item item = new Item();
        item.setId(itemDtoIn.getId());
        item.setName(itemDtoIn.getName());
        item.setDescription(itemDtoIn.getDescription());
        item.setAvailable(itemDtoIn.getAvailable());
        if (!userRepository.existsUserById(itemDtoIn.getUserId())) {
            throw new NotFoundException("Пользователь не найден");
        }
        item.setUser(userRepository.getUserById(itemDtoIn.getUserId()));
        if (itemDtoIn.getLastBookingId() != null && bookingRepository.existsById(itemDtoIn.getLastBookingId())) {
            item.setLastBooking(BookingMapper.toBookingShort(bookingRepository
                    .getBookingById(itemDtoIn.getLastBookingId())));
        }
        if (itemDtoIn.getNextBookingId() != null &&bookingRepository.existsById(itemDtoIn.getNextBookingId())) {
            item.setNextBooking(BookingMapper.toBookingShort(bookingRepository
                    .getBookingById(itemDtoIn.getNextBookingId())));
        }
        return item;
    }
}
