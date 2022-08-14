package ru.practicum.shareit.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;


    @Override
    public ItemDtoOut addComment(Long userId, Long itemId, Comment comment) {
        if (itemRepository.existsItemById(itemId) && userRepository.existsUserById(userId)
                && bookingRepository.existsBookingByBookerId(userId)) {
//            Item item = itemMapper.toItem(itemMapper.toDto(itemRepository.getItemById(itemId)));
            Booking booking = bookingRepository.getBookingById(itemRepository.getItemById(itemId).getLastBooking().getId());
            if (booking.getStatus().equals(Status.APPROVED)) {
                comment.setAuthor(userRepository.getUserById(userId));
                comment.setItem(itemRepository.getItemById(itemId));
                commentRepository.save(comment);
            } else {
                throw new ValidationException("Владелец не подтвердил бронирование");
            }
            return itemMapper.toDto(itemRepository.getItemById(itemId));
        } else {
            throw new ValidationException("Пользователь не использовал предмет");
        }
    }
}
