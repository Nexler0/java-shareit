package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingServiceTest {

    private final EntityManager em;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    private User user;
    private User user2;
    private Item item;
    private Booking booking;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Jef", "user@123.ru");
        userService.addUser(user);
        user2 = new User(2L, "Jass", "Jass@mail.ru");
        userService.addUser(user2);

        item = new Item();
        item.setId(1L);
        item.setName("Screw");
        item.setDescription("Regular Screw");
        item.setAvailable(true);
        item.setUser(user);
        itemService.addNewItem(user.getId(), item);

        booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStartDate(LocalDateTime.now().plusSeconds(2).withNano(0));
        booking.setEndDate(LocalDateTime.now().plusDays(1).withNano(0));
        booking.setId(1L);
        booking.setStatus(Status.WAITING);
    }

    @Test
    void createBookingTest() {
        bookingService.createBooking(user2.getId(), booking);

        TypedQuery<Booking> query = em.createQuery("select i from Booking i where i.id = :id",
                Booking.class);
        Booking check = query.setParameter("id", booking.getId()).getSingleResult();
        assertThat(check, equalTo(booking));
    }

    @Test
    void createBookingNotExistsItemTest() {
        booking.setItem(new Item());

        Throwable throwable = assertThrows(NotFoundException.class,
                () -> bookingService.createBooking(user2.getId(), booking));
        assertThat(throwable.getMessage(), is("Данный предмет не создан"));
    }

    @Test
    void createBookingWrongTimeTest() {
        booking.setStartDate(LocalDateTime.now().minusDays(2));

        Throwable throwable = assertThrows(ValidationException.class,
                () -> bookingService.createBooking(user2.getId(), booking));
        assertThat(throwable.getMessage(), is("Временной диапазон задан неверно"));
    }

    @Test
    void createBookingByItemOwnerTest() {
        booking.setBooker(user);

        Throwable throwable = assertThrows(NotFoundException.class,
                () -> bookingService.createBooking(user.getId(), booking));
        assertThat(throwable.getMessage(), is("Владелец не может арендовать у себя предмет"));
    }

    @Test
    void createBookingByNotExistUserTest() {
        Throwable throwable = assertThrows(NotFoundException.class,
                () -> bookingService.createBooking(10L, booking));
        assertThat(throwable.getMessage(), is("Пользователь не создан"));
    }

    @Test
    void createBookingNotAvailableItemTest() {
        item.setAvailable(false);
        itemService.updateItem(user.getId(), item.getId(), item);

        Throwable throwable = assertThrows(ValidationException.class,
                () -> bookingService.createBooking(user2.getId(), booking));
        assertThat(throwable.getMessage(), is("Предмет недоступен"));
    }

    @Test
    void setApproveToBookingTest() {
        bookingService.createBooking(user2.getId(), booking);
        booking.setStatus(Status.APPROVED);
        bookingService.setApproveStatusToBooking(user.getId(), booking.getId(), true);
        assertThat(bookingService.findBookingById(user2.getId(), booking.getId()), equalTo(booking));
    }

    @Test
    void setRejectedToBookingTest() {
        bookingService.createBooking(user2.getId(), booking);
        booking.setStatus(Status.REJECTED);
        bookingService.setApproveStatusToBooking(user.getId(), booking.getId(), false);
        assertThat(bookingService.findBookingById(user2.getId(), booking.getId()), equalTo(booking));
    }

    @Test
    void setApproveToBookingNotOwnerTest() {
        bookingService.createBooking(user2.getId(), booking);
        Throwable throwable = assertThrows(NotFoundException.class,
                () -> bookingService.setApproveStatusToBooking(user2.getId(), booking.getId(), true));
        assertThat(throwable.getMessage(), is("Пользователь не является владельцем предмета"));
    }

    @Test
    void setApproveToBookingAlreadyApproveTest() {
        bookingService.createBooking(user2.getId(), booking);
        bookingService.setApproveStatusToBooking(user.getId(), booking.getId(), true);
        Throwable throwable = assertThrows(ValidationException.class,
                () -> bookingService.setApproveStatusToBooking(user.getId(), booking.getId(), true));
        assertThat(throwable.getMessage(), is("Уже подтверждено"));
    }

    @Test
    void findAllBookingWrongStatusTest() {
        Throwable throwable = assertThrows(ValidationException.class,
                () -> bookingService.findAllBooking(user.getId(), "ALG", 0, 10));
        assertThat(throwable.getMessage(), is("Unknown state: ALG"));
    }

    @Test
    void findAllBookingNotExistUserTest() {
        Throwable throwable = assertThrows(NotFoundException.class,
                () -> bookingService.findAllBooking(10L, "ALL", 0, 10));
        assertThat(throwable.getMessage(), is("Пользователь не найден"));
    }

    @Test
    void findAllUserBookingWrongStatusTest() {
        Throwable throwable = assertThrows(ValidationException.class,
                () -> bookingService.findAllUserBooking(user.getId(), "ALG", 0, 10));
        assertThat(throwable.getMessage(), is("Unknown state: ALG"));
    }

    @Test
    void findAllUserBookingNotExistUserTest() {
        Throwable throwable = assertThrows(NotFoundException.class,
                () -> bookingService.findAllUserBooking(10L, "ALL", 0, 10));
        assertThat(throwable.getMessage(), is("Пользователь не найден"));
    }

    @Test
    void findBookingByWrongUserIdTest() {
        bookingService.createBooking(user2.getId(), booking);
        Throwable throwable = assertThrows(NotFoundException.class,
                () -> bookingService.findBookingById(10L, booking.getId()));
        assertThat(throwable.getMessage(), is("Владельцем бронирования пользователь не является"));
    }

    @Test
    void findBookingByIdNotCreatedTest() {
        Throwable throwable = assertThrows(NotFoundException.class,
                () -> bookingService.findBookingById(user2.getId(), booking.getId()));
        assertThat(throwable.getMessage(), is("Бронирование не создано"));
    }

    @Test
    void findAllBookingTest() {
        item.setLastBooking(BookingMapper.toBookingShort(booking));
        itemService.updateItem(user.getId(), item.getId(), item);
        bookingService.createBooking(user2.getId(), booking);
        User user3 = userService.addUser(new User(3L, "Jasmin", "Jasmin@mil.ru"));
        User user4 = userService.addUser(new User(4L, "Ostin", "Ostin@mil.ru"));
        Booking booking2 = new Booking();
        booking2.setBooker(user2);
        booking2.setItem(item);
        booking2.setStartDate(LocalDateTime.now().plusSeconds(1).withNano(0));
        booking2.setEndDate(LocalDateTime.now().plusDays(1).withNano(0));
        booking2.setId(2L);
        booking2.setStatus(Status.REJECTED);
        bookingService.createBooking(user3.getId(), booking2);

        assertThat(bookingService.findAllBooking(user4.getId(), "ALL", 0, 10),
                equalTo(List.of(booking, booking2)));
        assertThat(bookingService.findAllBooking(user4.getId(), "CURRENT", 0, 10),
                equalTo(List.of()));
        assertThat(bookingService.findAllBooking(user4.getId(), "PAST", 0, 10),
                equalTo(List.of()));
        assertThat(bookingService.findAllBooking(user4.getId(), "FUTURE", 0, 10),
                equalTo(List.of(booking, booking2)));
        assertThat(bookingService.findAllBooking(user4.getId(), "WAITING", 0, 10),
                equalTo(List.of(booking)));
        assertThat(bookingService.findAllBooking(user4.getId(), "REJECTED", 0, 10),
                equalTo(List.of(booking2)));
    }

    @Test
    void findAllUserBookingTest() {
        item.setLastBooking(BookingMapper.toBookingShort(booking));
        itemService.updateItem(user.getId(), item.getId(), item);
        bookingService.createBooking(user2.getId(), booking);
        User user3 = userService.addUser(new User(3L, "Jasmin", "Jasmin@mil.ru"));
        Booking booking2 = new Booking();
        booking2.setBooker(user2);
        booking2.setItem(item);
        booking2.setStartDate(LocalDateTime.now().plusSeconds(2).withNano(0));
        booking2.setEndDate(LocalDateTime.now().plusDays(1).withNano(0));
        booking2.setId(2L);
        booking2.setStatus(Status.REJECTED);
        bookingService.createBooking(user3.getId(), booking2);

        assertThat(bookingService.findBookings(), equalTo(List.of(booking, booking2)));
        assertThat(bookingService.findAllUserBooking(user.getId(), "ALL", 0, 10),
                equalTo(List.of(booking, booking2)));
        assertThat(bookingService.findAllUserBooking(user.getId(), "CURRENT", 0, 10),
                equalTo(List.of()));
        assertThat(bookingService.findAllUserBooking(user.getId(), "PAST", 0, 10),
                equalTo(List.of()));
        assertThat(bookingService.findAllUserBooking(user.getId(), "FUTURE", 0, 10),
                equalTo(List.of(booking, booking2)));
        assertThat(bookingService.findAllUserBooking(user.getId(), "WAITING", 0, 10),
                equalTo(List.of(booking)));
        assertThat(bookingService.findAllUserBooking(user.getId(), "REJECTED", 0, 10),
                equalTo(List.of(booking2)));
    }
}
