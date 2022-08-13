package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<Booking> findAllBooking(Long userId, String status) {
        if (userRepository.existsUserById(userId)) {
            switch (status) {
                case "ALL":
                    return bookingRepository.findAll();
                case "CURRENT":
                    return bookingRepository.getAllByStartDateBeforeOrderByStartDateDesc(LocalDateTime.now());
                case "**PAST**":
                    return bookingRepository.getAllByEndDateBeforeOrderByStartDateDesc(LocalDateTime.now());
                case "FUTURE":
                    return bookingRepository.getAllByStartDateAfterOrderByStartDateDesc(LocalDateTime.now());
                case "WAITING":
                    return bookingRepository.getAllByStatusContainsOrderByStartDateDesc("WAITING");
                case "REJECTED":
                    return bookingRepository.getAllByStatusContainsOrderByStartDateDesc("REJECTED");
                default:
                    throw new ValidationException("Неизвесный статус поиска");
            }
        }
        throw new NotFoundException("Пользователь не найден");
    }

    @Override
    public List<Booking> findAllUserBooking(Long userId, String status) {
        if (userRepository.existsUserById(userId)) {
            switch (status) {
                case "ALL":
                    return bookingRepository.getBookingByItemUserIdOrderByStartDateDesc(userId);
                case "CURRENT":
                    return bookingRepository.getBookingByItemUserIdAndStartDateBeforeOrderByStartDateDesc(userId,
                            LocalDateTime.now());
                case "**PAST**":
                    return bookingRepository.getBookingByItemUserIdAndEndDateBeforeOrderByStartDateDesc(userId,
                            LocalDateTime.now());
                case "FUTURE":
                    return bookingRepository.getBookingByItemUserIdAndStartDateAfterOrderByStartDateDesc(userId,
                            LocalDateTime.now());
                case "WAITING":
                    return bookingRepository.getBookingByItemUserIdAndStatusContainsOrderByStartDateDesc("WAITING");
                case "REJECTED":
                    return bookingRepository.getBookingByItemUserIdAndStatusContainsOrderByStartDateDesc("REJECTED");
                default:
                    throw new ValidationException("Unknown state: " + status);
            }
        }
        throw new NotFoundException("Пользователь не найден");
    }

    @Override
    public Booking findBookingById(Long userId, Long bookingId) {
        if (bookingRepository.existsById(bookingId)) {
            Booking booking = bookingRepository.getBookingById(bookingId);
            if (userRepository.existsUserById(userId) && booking.getBooker().getId().equals(userId)
                    || booking.getItem().getUser().getId().equals(userId)) {
                return booking;
            } else {
                throw new NotFoundException("Владельцем бронирования пользователь не является");
            }
        } else {
            throw new NotFoundException("Бронирование не создано");
        }
    }

    @Override
    public Booking createBooking(Long userId, Booking booking) {
        LocalDateTime now = LocalDateTime.now().withNano(0);
        if (booking.getStartDate().isBefore(now) || booking.getEndDate().isBefore(now)) {
            throw new ValidationException("Временной диапазон неверно задан");
        }
        if (userId.equals(booking.getItem().getUser().getId())) {
            throw new NotFoundException("Владелец не может арендовать у себя предмет");
        }
        User user = userRepository.getUserById(userId);
        if (user != null) {
            booking.setBooker(user);
        } else {
            throw new NotFoundException("Пользователь не создан");
        }
        Item item = booking.getItem();
        if (item != null) {
            if (item.getAvailable()) {
                booking.setItem(item);
            } else {
                throw new ValidationException("Предмет недоступен");
            }
        } else {
            throw new NotFoundException("Такой предмет не создан");
        }

        if (booking.getStatus() == null) {
            booking.setStatus(Status.WAITING);
        }
        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public Booking approveBookingByOwner(Long userId, Long bookingId, Boolean approve) {
        Booking booking = bookingRepository.getBookingById(bookingId);
        if (userRepository.existsUserById(userId)
                && booking.getItem().getUser().getId().equals(userId)) {
            if (approve && booking.getApproved()){
                throw new ValidationException("Уже тодтверждено");
            }
            if (approve) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
            bookingRepository.setBookingInfoById(booking.getStatus(), booking.getId());
            return booking;
        }
        throw new NotFoundException("Пользователь не является владельцем");
    }


}
