package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.model.CommentMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public ItemDtoOut toDto(Item item) {
        ItemDtoOut result = ItemDtoOut.builder()
                .id(item.getId())
                .userId(item.getUser().getId())
                .requestId(null)
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
        if (item.getLastBooking() != null) {
            if (item.getLastBooking().getStatus().equals(Status.CANCELED)
                    || item.getLastBooking().getStatus().equals(Status.REJECTED)) {
                result.setLastBooking(item.getLastBooking());
            } else {
                result.setNextBooking(item.getLastBooking());
            }
        }
        if (item.getLastBooking() != null && item.getNextBooking() != null) {
            result.setLastBooking(item.getLastBooking());
            result.setNextBooking(item.getNextBooking());
        }
        if (commentRepository.existsCommentByItemId(item.getId())) {
            result.setComments(CommentMapper.toCommentShort(commentRepository.getAllByItemId(item.getId())));
        } else {
            result.setComments(new ArrayList<>());
        }
        return result;
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
        if (itemDtoIn.getNextBookingId() != null && bookingRepository.existsById(itemDtoIn.getNextBookingId())) {
            item.setNextBooking(BookingMapper.toBookingShort(bookingRepository
                    .getBookingById(itemDtoIn.getNextBookingId())));
        }
        if (commentRepository.existsCommentByItemId(item.getId())) {
            item.setComments(CommentMapper.toCommentShort(commentRepository.getAllByItemId(item.getId())));
        }
        return item;
    }
}
