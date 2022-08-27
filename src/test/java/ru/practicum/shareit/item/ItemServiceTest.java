package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
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
public class ItemServiceTest {

    private final EntityManager em;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    private User user;
    private User user2;
    private Item item;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Jon", "Jon@mail.ru");
        userService.addUser(user);
        user2 = new User(2L, "Jass", "Jass@mail.ru");
        userService.addUser(user2);

        item = new Item();
        item.setId(1L);
        item.setName("Screw");
        item.setDescription("Regular screw");
        item.setAvailable(true);
        item.setUser(user);
    }

    @Test
    void addNewItemTest() {
        itemService.addNewItem(user.getId(), item);

        TypedQuery<Item> query = em.createQuery("select i from Item i where i.id = :id",
                Item.class);
        Item checkIR = query.setParameter("id", item.getId()).getSingleResult();
        assertThat(checkIR, equalTo(item));
    }

    @Test
    void addNewItemWithoutUserTest() {
        Throwable throwable = assertThrows(NotFoundException.class,
                () -> itemService.addNewItem(5L, item));
        assertThat(throwable.getMessage(), is("Пользователь не найден"));
    }

    @Test
    void addNewItemWithoutNameTest() {
        item.setName(null);
        Throwable throwable = assertThrows(ValidationException.class,
                () -> itemService.addNewItem(1L, item));
        assertThat(throwable.getMessage(), is("Входные условия item"));
    }

    @Test
    void addNewItemWithoutDescriptionTest() {
        item.setDescription(null);
        Throwable throwable = assertThrows(ValidationException.class,
                () -> itemService.addNewItem(1L, item));
        assertThat(throwable.getMessage(), is("Входные условия item"));
    }

    @Test
    void deleteItemTest() {
        itemService.addNewItem(user.getId(), item);
        itemService.deleteItem(user.getId(), item.getId());
        Throwable throwable = assertThrows(NotFoundException.class,
                () -> itemService.getItemById(1L, item.getId()));
        assertThat(throwable.getMessage(), is("Item not found"));
    }

    @Test
    void findItemByRequestTest() {
        itemService.addNewItem(user.getId(), item);
        assertThat(itemService.findItemByRequest("screw"), equalTo(List.of(item)));
    }

    @Test
    void getAllItemsTest() {
        itemService.addNewItem(user.getId(), item);
        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStartDate(LocalDateTime.now().withNano(0));
        booking.setEndDate(LocalDateTime.now().plusDays(1).withNano(0));
        bookingService.createBooking(user2.getId(), booking);
        item.setLastBooking(BookingMapper.toBookingShort(booking));
        assertThat(itemService.getAllItems(), equalTo(List.of(item)));
    }

    @Test
    void updateItemTest() {
        itemService.addNewItem(user.getId(), item);
        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStartDate(LocalDateTime.now().withNano(0));
        booking.setEndDate(LocalDateTime.now().plusDays(1).withNano(0));
        bookingService.createBooking(user2.getId(), booking);
        item.setName("New cool name");
        item.setDescription("Cool description");
        item.setLastBooking(BookingMapper.toBookingShort(booking));
        assertThat(itemService.updateItem(user.getId(), item.getId(), item), equalTo(item));
    }
}
