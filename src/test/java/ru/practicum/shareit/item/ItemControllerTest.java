package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.comment.CommentService;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
//@Transactional
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@WebMvcTest(controllers = {ItemController.class})
public class ItemControllerTest {

    @MockBean
    private ItemService itemService;
    @MockBean
    private CommentService commentService;
    @MockBean
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemController itemController;

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();
    private Item item;
    private ItemDtoIn itemDtoIn;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();

        item = new Item();
        item.setId(1L);
        item.setName("Screw");
        item.setDescription("Regular Screw");
        item.setAvailable(true);

        itemDtoIn = new ItemDtoIn();
        itemDtoIn.setId(item.getId());
        itemDtoIn.setName(item.getName());
        itemDtoIn.setAvailable(item.getAvailable());
        itemDtoIn.setDescription(item.getDescription());
    }

    @Test
    void addItemTest() throws Exception {

//        when(userService.addUser(any()))
//                .thenReturn(new User(1L, "jef", "jef@mail.ru"));

//        userService.addUser(new User(1L, "jef", "jef@mail.ru"));

        when(itemService.addNewItem(1L, item)).thenReturn(item);

//        when(itemMapper.toItem(itemDtoIn)).thenReturn(item);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
//                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())));
    }
}
