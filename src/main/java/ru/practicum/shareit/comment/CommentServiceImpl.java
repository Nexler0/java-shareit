package ru.practicum.shareit.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.model.CommentMapper;
import ru.practicum.shareit.comment.model.CommentShort;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    public CommentShort addComment(Long userId, Long itemId, Comment comment) {
        if (itemRepository.existsItemById(itemId) && userRepository.existsUserById(userId)
                && bookingRepository.existsBookingByBookerId(userId)) {
            List<Booking> bookings = bookingRepository.getBookingsByItemId(itemId);
            Booking booking = bookings.get(0);
            if (booking.getStatus().equals(Status.APPROVED)) {
                comment.setAuthor(userRepository.getUserById(userId));
                comment.setItem(itemRepository.getItemById(itemId));
            } else {
                throw new ValidationException("Владелец не подтвердил бронирование");
            }
            return CommentMapper.toCommentShort(commentRepository.save(comment));
        } else {
            throw new ValidationException("Пользователь не использовал предмет");
        }
    }
}
