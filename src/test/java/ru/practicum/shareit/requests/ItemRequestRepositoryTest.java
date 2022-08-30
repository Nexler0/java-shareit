package ru.practicum.shareit.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private UserRepository userRepository;

    private ItemRequest itemRequest;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("user@123.ru");
        user.setName("Jef");
        userRepository.save(user);
        userRepository.save(new User(null, "Jass", "Jass@mail.ru"));

        itemRequest = new ItemRequest();
        itemRequest.setRequester(user);
        itemRequest.setDescription("Need screw");
        itemRequest.setId(1L);
        itemRequestRepository.save(itemRequest);
    }

    @Test
    void findAllItemRequestTest() {
        assertThat(List.of(itemRequest).get(0), equalTo(itemRequestRepository
                .findAll(PageRequest.of(0, 10)).toList().get(0)));
    }

    @Test
    void getItemRequestByIdTest() {
        assertThat(itemRequest, equalTo(itemRequestRepository.getItemRequestById(itemRequest.getId())));
    }

    @Test
    void findItemRequestByRequesterIdTest() {
        assertThat(List.of(itemRequest), equalTo(itemRequestRepository
                .findItemRequestByRequesterId(user.getId())));
    }
}
