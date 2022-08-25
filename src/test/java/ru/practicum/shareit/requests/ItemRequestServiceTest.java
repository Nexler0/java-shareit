package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestServiceTest {

    private final EntityManager em;
    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Jon", "Jon@mail.ru");
        item = new Item();
        item.setId(1L);
        item.setName("Screw");
        item.setDescription("Regular screw");
        item.setAvailable(true);
        item.setUser(user);
    }

    @Test
    void addRequestFromUserTest() {
        userService.addUser(user);
        itemService.addNewItem(user.getId(), item);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("Need screw");
        itemRequest.setRequester(user);
        itemRequest.setItems(List.of(itemMapper.toShort(item)));

        itemRequestService.addRequestFromUser(1L, itemRequest);
        TypedQuery<ItemRequest> query = em.createQuery("select i from ItemRequest i where i.id = :id",
                ItemRequest.class);
        ItemRequest checkIR = query.setParameter("id", itemRequest.getId()).getSingleResult();
        assertThat(checkIR, equalTo(itemRequest));
    }

    @Test
    void addInvalidItemRequestNullUserTest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("Need screw");
        itemRequest.setRequester(null);
        itemRequest.setItems(List.of(itemMapper.toShort(item)));

        Throwable throwable = assertThrows(NotFoundException.class,
                () -> itemRequestService.addRequestFromUser(1L, itemRequest));
        assertThat(throwable.getMessage(), is("Пользователь не найден"));
    }

    @Test
    void addInvalidItemRequestNullDescriptionTest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(null);
        itemRequest.setRequester(user);
        itemRequest.setItems(List.of(itemMapper.toShort(item)));

        Throwable throwable = assertThrows(ValidationException.class,
                () -> itemRequestService.addRequestFromUser(1L, itemRequest));
        assertThat(throwable.getMessage(), is("Пустой запрос"));
    }

    @Test
    void getAllRequestByOwnerTest() {
        userService.addUser(user);
        itemService.addNewItem(user.getId(), item);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("Need screw");
        itemRequest.setRequester(user);
        itemRequest.setItems(List.of(itemMapper.toShort(item)));
        itemRequestService.addRequestFromUser(user.getId(), itemRequest);

        assertThat(itemRequestService.getAllRequestsByOwner(user.getId()), equalTo(List.of(itemRequest)));
    }

    @Test
    void getAllRequestByNotExistUserTest() {
        Throwable throwable = assertThrows(NotFoundException.class,
                () -> itemRequestService.getAllRequestsByOwner(50L));
        assertThat(throwable.getMessage(), is("Пользователь не найден"));
    }

    @Test
    void getAllRequestTest() {
        User user = userService.addUser(new User(1L, "Fran", "Fran@mail.ru"));
        User user1 = userService.addUser(new User(2L, "Genry", "Genry@mail.ru"));
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequester(user);
        itemRequest.setDescription("Need screw");
        itemRequestService.addRequestFromUser(user.getId(), itemRequest);
        ItemRequest itemRequest2 = new ItemRequest();
        itemRequest2.setRequester(user);
        itemRequest2.setDescription("Need screw +");
        itemRequestService.addRequestFromUser(user.getId(), itemRequest2);
        ItemRequest itemRequest3 = new ItemRequest();
        itemRequest3.setRequester(user);
        itemRequest3.setDescription("Need screw ++");
        itemRequestService.addRequestFromUser(user.getId(), itemRequest3);

        assertThat(itemRequestService.getAllRequests(user1.getId(), 0, 10),
                equalTo(List.of(itemRequest, itemRequest2, itemRequest3)));

    }

    @Test
    void getEmptyRequestTest() {
        User user = userService.addUser(new User(1L, "Fran", "Fran@mail.ru"));
        assertThat(itemRequestService.getAllRequests(user.getId(), 0, 10),
                equalTo(List.of()));
    }

    @Test
    void getItemRequestByIdTest() {
        userService.addUser(user);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequester(user);
        itemRequest.setDescription("Need screw");
        itemRequestService.addRequestFromUser(user.getId(), itemRequest);
        ItemRequest itemRequest2 = new ItemRequest();
        itemRequest2.setRequester(user);
        itemRequest2.setDescription("Need screw +");
        itemRequestService.addRequestFromUser(user.getId(), itemRequest2);
        ItemRequest itemRequest3 = new ItemRequest();
        itemRequest3.setRequester(user);
        itemRequest3.setDescription("Need screw ++");
        itemRequestService.addRequestFromUser(user.getId(), itemRequest3);

        assertThat(itemRequestService.getItemRequestsById(user.getId(), itemRequest2.getId()), equalTo(itemRequest2));
    }

    @Test
    void getItemRequestByNotExistIdTest() {
        userService.addUser(user);
        Throwable throwable = assertThrows(NotFoundException.class,
                () -> itemRequestService.getItemRequestsById(user.getId(), 50L));
        assertThat(throwable.getMessage(), is("Неверные условия поиска"));
    }
}
