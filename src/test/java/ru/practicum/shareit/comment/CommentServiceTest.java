package ru.practicum.shareit.comment;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CommentServiceTest {

    private final EntityManager em;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;
    private final CommentService commentService;

    private User user;
    private User user2;
    private Item item;
    private Booking booking;
    private Comment comment;

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
        bookingService.createBooking(user2.getId(), booking);

        comment = new Comment();
        comment.setAuthor(user2);
        comment.setItem(item);
        comment.setText("Nice screw");
        comment.setId(1L);
    }

    @Test
    void addNewCommentTest() {
        bookingService.setApproveStatusToBooking(user.getId(), booking.getId(), true);
        commentService.addComment(user2.getId(), item.getId(), comment);

        TypedQuery<Comment> query = em.createQuery("select i from Comment i where i.id = :id", Comment.class);
        Comment check = query.setParameter("id", comment.getId()).getSingleResult();
        assertThat(check, equalTo(comment));
    }

    @Test
    void addNewCommentWithoutApproveTest() {
        Throwable throwable = assertThrows(ValidationException.class,
                () -> commentService.addComment(user2.getId(), item.getId(), comment));
        assertThat(throwable.getMessage(), is("Владелец не подтвердил бронирование"));
    }

    @Test
    void addNewCommentNotUsedItemTest() {
        Throwable throwable = assertThrows(ValidationException.class,
                () -> commentService.addComment(user.getId(), item.getId(), comment));
        assertThat(throwable.getMessage(), is("Пользователь не использовал предмет"));
    }
}
