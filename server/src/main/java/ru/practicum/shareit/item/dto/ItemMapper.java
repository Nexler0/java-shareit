package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.model.CommentMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemShort;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Slf4j
public class ItemMapper {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;

    public ItemDtoOut toDto(Item item) {
        ItemDtoOut result = ItemDtoOut.builder()
                .id(item.getId())
                .userId(item.getUser().getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
        if (item.getItemRequest() != null) {
            result.setRequestId(item.getItemRequest().getId());
        }
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
        if (itemRepository.existsItemById(itemDtoIn.getId())) {
            item = itemRepository.getItemById(itemDtoIn.getId());
            if (!item.getUser().getId().equals(itemDtoIn.getUserId())) {
                throw new NotFoundException("Пользователь не является владельцем предмета");
            }
        }
        if (itemDtoIn.getName() != null) {
            item.setName(itemDtoIn.getName());
        }
        if (itemDtoIn.getDescription() != null) {
            item.setDescription(itemDtoIn.getDescription());
        }
        if (itemDtoIn.getAvailable() != null) {
            item.setAvailable(itemDtoIn.getAvailable());
        }
        if (itemDtoIn.getRequestId() != null) {
            item.setItemRequest(itemRequestRepository.getReferenceById(itemDtoIn.getRequestId()));
        }
        if (!userRepository.existsUserById(itemDtoIn.getUserId())
                && !item.getUser().getId().equals(itemDtoIn.getUserId())) {
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

    public ItemShort toShort(Item item) {
        ItemShort result = new ItemShort();
        result.setId(item.getId());
        result.setName(item.getName());
        result.setDescription(item.getDescription());
        result.setOwnerId(item.getUser().getId());
        result.setAvailable(item.getAvailable());
        if (item.getItemRequest() != null) {
            result.setRequestId(item.getItemRequest().getId());
        }
        return result;
    }
}
