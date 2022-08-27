package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    private Booking booking;

    @BeforeEach
    void setUp() {

        User user = new User(1L, "Jef", "user@123.ru");
        userRepository.save(user);
        User user2 = new User(2L, "Jass", "Jass@mail.ru");
        userRepository.save(user2);

        Item item = new Item();
        item.setId(1L);
        item.setName("Screw");
        item.setDescription("Regular Screw");
        item.setAvailable(true);
        item.setUser(user);
        itemRepository.save(item);

        booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStartDate(LocalDateTime.now().withNano(0));
        booking.setEndDate(LocalDateTime.now().plusDays(1).withNano(0));
        booking.setId(1L);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
    }

    @Test
    void findAllPageableTest() {
        assertThat(bookingRepository.findAll(PageRequest.of(0, 10)).toList(), equalTo(List.of(booking)));
    }

    @Test
    void findAllTest() {
        assertThat(bookingRepository.findAll(), equalTo(List.of(booking)));
    }

    @Test
    void getBookingByIdTest() {
        assertThat(bookingRepository.getBookingById(booking.getId()), equalTo(booking));
    }

    @Test
    void setBookingInfoByIdTest() {
        booking.setStatus(Status.REJECTED);
        bookingRepository.setBookingInfoById(Status.REJECTED, booking.getId());
        bookingRepository.save(booking);
        Booking check = bookingRepository.getBookingById(booking.getId());
        assertThat(check, equalTo(booking));
    }

    @Test
    void getAllBookingBeforeStartDateTest() {
        assertThat(bookingRepository.getAllBookingBeforeStartDate(LocalDateTime.now().withNano(0),
                PageRequest.of(0, 10)).toList(), equalTo(List.of(booking)));
    }

    @Test
    void getAllBookingBeforeEndDateTest() {
        assertThat(bookingRepository.getAllBookingBeforeEndDate(LocalDateTime.now().withNano(0),
                PageRequest.of(0, 10)).toList(), equalTo(List.of()));
    }

    @Test
    void getAllBookingAfterStartDateTest() {
        assertThat(bookingRepository.getAllBookingAfterStartDate(LocalDateTime.now().withNano(0),
                PageRequest.of(0, 10)).toList(), equalTo(List.of()));
    }

    @Test
    void getBookingsByItemUserIdTest() {
        assertThat(bookingRepository.getBookingsByItemUserId(1L, PageRequest.of(0, 10)).toList(),
                equalTo(List.of(booking)));
    }

    @Test
    void getBookingsByItemUserIdBeforeStartDateTest() {
        assertThat(bookingRepository.getBookingsByItemUserIdBeforeStartDate(1L, LocalDateTime.now().withNano(0),
                PageRequest.of(0, 10)).toList(), equalTo(List.of(booking)));
    }

    @Test
    void getBookingsByItemUserIdBeforeEndDateTest() {
        assertThat(bookingRepository.getBookingsByItemUserIdBeforeEndDate(1L, LocalDateTime.now().withNano(0),
                PageRequest.of(0, 10)).toList(), equalTo(List.of()));
    }

    @Test
    void getBookingsByItemUserIdAfterStartDateTest() {
        assertThat(bookingRepository.getBookingsByItemUserIdAfterStartDate(1L, LocalDateTime.now().withNano(0),
                PageRequest.of(0, 10)).toList(), equalTo(List.of()));
    }

    @Test
    void existsBookingByBookerIdTest() {
        assertThat(bookingRepository.existsBookingByBookerId(2L), equalTo(true));
        assertThat(bookingRepository.existsBookingByBookerId(1L), equalTo(false));
    }
}
