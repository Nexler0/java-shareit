package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.CommentService;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.model.CommentMapper;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
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
    private final ItemMapper itemMapper;
    private final CommentService commentService;

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
        assertThat(throwable.getMessage(), is("Предмет не найден"));
    }

    @Test
    void deleteItemByNotOwnerTest() {
        itemService.addNewItem(user.getId(), item);
        Throwable throwable = assertThrows(AccessException.class,
                () -> itemService.deleteItem(2L, item.getId()));
        assertThat(throwable.getMessage(), is("Доступ запрещен"));
    }

    @Test
    void findItemByRequestTest() {
        itemService.addNewItem(user.getId(), item);
        assertThat(itemService.findItemByRequest("screw"), equalTo(List.of(item)));
    }

    @Test
    void findItemByEmptyRequestTest() {
        itemService.addNewItem(user.getId(), item);
        assertThat(itemService.findItemByRequest(""), equalTo(List.of()));
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
    void getItemByUserIdTest() {
        itemService.addNewItem(user.getId(), item);
        assertThat(itemService.getItemsByUserId(0L), equalTo(List.of(item)));
    }

    @Test
    void getItemByIdTest() {
        itemService.addNewItem(user.getId(), item);
        assertThat(itemService.getItemById(0L, item.getId()), equalTo(item));
    }

    @Test
    void updateItemFailTest() {
        itemService.addNewItem(user.getId(), item);
        Throwable throwable = assertThrows(NotFoundException.class,
                () -> itemService.updateItem(0L, item.getId(), item));
        assertThat(throwable.getMessage(), is("Ошибка входных параметров"));
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

    @Test
    void itemMapperTest() {
        itemService.addNewItem(user.getId(), item);
        Booking booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStartDate(LocalDateTime.now().withNano(0));
        booking.setEndDate(LocalDateTime.now().plusDays(1).withNano(0));
        bookingService.createBooking(user2.getId(), booking);
        bookingService.setApproveStatusToBooking(user.getId(), booking.getId(), true);
        item.setLastBooking(BookingMapper.toBookingShort(booking));
        Comment comment = new Comment();
        comment.setItem(item);
        comment.setText("Nice staff");
        comment.setAuthor(user2);
        commentService.addComment(user2.getId(), item.getId(), comment);
        item.setComments(List.of(CommentMapper.toCommentShort(comment)));
        ItemDtoOut itemDtoOut = ItemDtoOut.builder()
                .id(item.getId())
                .userId(item.getUser().getId())
                .name(item.getName())
                .description(item.getDescription())
                .comments(item.getComments())
                .nextBooking(item.getLastBooking())
                .available(item.getAvailable())
                .build();
        assertThat(itemMapper.toDto(item), equalTo(itemDtoOut));
        ItemDtoIn itemDtoIn = new ItemDtoIn();
        itemDtoIn.setUserId(item.getUser().getId());
        itemDtoIn.setDescription(item.getDescription());
        itemDtoIn.setName(item.getName());
        itemDtoIn.setAvailable(item.getAvailable());
        itemDtoIn.setLastBookingId(booking.getId());
        itemDtoIn.setId(item.getId());
        assertThat(itemMapper.toItem(itemDtoIn), equalTo(item));
    }
}
