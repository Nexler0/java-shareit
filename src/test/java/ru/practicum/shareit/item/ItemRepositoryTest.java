package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.ItemRequestRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private Item item;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Jon", "Jon@mail.ru");
        userRepository.save(user);
        User user2 = new User(2L, "Jass", "Jass@mail.ru");
        userRepository.save(user2);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequester(user2);
        itemRequest.setDescription("need screw");
        itemRequestRepository.save(itemRequest);

        item = new Item();
        item.setId(1L);
        item.setName("Screw");
        item.setDescription("Regular screw");
        item.setAvailable(true);
        item.setUser(user);
        item.setItemRequest(itemRequest);
        itemRepository.save(item);
    }

    @Test
    void findItemByUserIdTest() {
        assertThat(itemRepository.findItemByUserId(user.getId()), equalTo(List.of(item)));
    }

    @Test
    void getItemById() {
        assertThat(itemRepository.getItemById(item.getId()), equalTo(item));
    }

    @Test
    void getAllByItemRequestIdTest() {
        assertThat(itemRepository.getAllByItemRequestId(1L, PageRequest.of(0, 100)).toList(),
                equalTo(List.of(item)));
    }

    @Test
    void searchTest() {
        assertThat(itemRepository.search("regular"), equalTo(List.of(item)));
    }

    @Test
    void existsItemByIdTest() {
        assertThat(itemRepository.existsItemById(item.getId()), equalTo(true));
        assertThat(itemRepository.existsItemById(10L), equalTo(false));
    }
}
