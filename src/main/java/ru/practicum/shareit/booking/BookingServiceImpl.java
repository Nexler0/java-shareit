package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
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
import java.util.stream.Collectors;

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
                    return bookingRepository.getAllBookingBeforeStartDate(LocalDateTime.now())
                            .stream().filter(booking -> booking.getStatus().equals(Status.REJECTED)
                            ).collect(Collectors.toList());
                case "PAST":
                    return bookingRepository.getAllBookingBeforeEndDate(LocalDateTime.now());
                case "FUTURE":
                    return bookingRepository.getAllBookingAfterStartDate(LocalDateTime.now());
                case "WAITING":
                    return bookingRepository.getAllBookingAfterStartDate(LocalDateTime.now()).stream()
                            .filter(booking -> booking.getStatus() == Status.WAITING && !booking.getItem()
                                    .getUser().getId().equals(userId)).collect(Collectors.toList());
                case "REJECTED":
                    return bookingRepository.getAllBookingAfterStartDate(LocalDateTime.now()).stream()
                            .filter(booking -> booking.getStatus() == Status.REJECTED && !booking.getItem()
                                    .getUser().getId().equals(userId)).collect(Collectors.toList());
                default:
                    throw new ValidationException("Unknown state: " + status);
            }
        }
        throw new NotFoundException("Пользователь не найден");
    }

    @Override
    public List<Booking> findAllUserBooking(Long userId, String status) {
        if (userRepository.existsUserById(userId)) {
            switch (status) {
                case "ALL":
                    return bookingRepository.getBookingsByItemUserId(userId);
                case "CURRENT":
                    return bookingRepository.getBookingsByItemUserIdBeforeStartDate(userId,
                            LocalDateTime.now()).stream().filter(booking -> booking.getStatus().equals(Status.REJECTED)
                    ).collect(Collectors.toList());
                case "PAST":
                    return bookingRepository.getBookingsByItemUserIdBeforeEndDate(userId,
                            LocalDateTime.now());
                case "FUTURE":
                    return bookingRepository.getBookingsByItemUserIdAfterStartDate(userId,
                            LocalDateTime.now());
                case "WAITING":
                    return bookingRepository.getAllBookingAfterStartDate(LocalDateTime.now()).stream()
                            .filter(booking -> booking.getStatus() == Status.WAITING && booking.getItem()
                                    .getUser().getId().equals(userId)).collect(Collectors.toList());
                case "REJECTED":
                    return bookingRepository.getAllBookingAfterStartDate(LocalDateTime.now()).stream()
                            .filter(booking -> booking.getStatus() == Status.REJECTED && booking.getItem().getUser()
                                    .getId().equals(userId)).collect(Collectors.toList());
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

    @Transactional
    @Override
    public Booking createBooking(Long userId, Booking booking) {
        LocalDateTime now = LocalDateTime.now().withNano(0);

        if (booking.getStartDate().isBefore(now) || booking.getEndDate().isBefore(now)) {
            throw new ValidationException("Временной диапазон задан неверно");
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
        if (booking.getStatus() == null) {
            booking.setStatus(Status.WAITING);
        }

        Item item = itemRepository.getItemById(booking.getItem().getId());
        Booking bookingSaved;

        if (item != null) {
            if (item.getAvailable()) {
                bookingSaved = bookingRepository.save(booking);
                if (item.getNextBooking() == null) {
                    item.setLastBooking(BookingMapper.toBookingShort(bookingSaved));
                } else {
                    item.setNextBooking(item.getNextBooking());
                    item.setLastBooking(BookingMapper.toBookingShort(bookingSaved));
                }
                return bookingSaved;
            } else {
                throw new ValidationException("Предмет недоступен");
            }
        } else {
            throw new NotFoundException("Данный предмет не создан");
        }
    }

    @Transactional
    @Override
    public Booking setApproveStatusToBooking(Long userId, Long bookingId, Boolean approve) {
        Booking booking = bookingRepository.getBookingById(bookingId);

        if (userRepository.existsUserById(userId)
                && booking.getItem().getUser().getId().equals(userId)) {
            if (booking.getStatus().equals(Status.APPROVED)) {
                throw new ValidationException("Уже подтверждено");
            }
            if (approve) {
                booking.setStatus(Status.APPROVED);
            } else {
                booking.setStatus(Status.REJECTED);
            }
            bookingRepository.setBookingInfoById(booking.getStatus(), booking.getId());
            return booking;
        }
        throw new NotFoundException("Пользователь не является владельцем предмета");
    }
}
