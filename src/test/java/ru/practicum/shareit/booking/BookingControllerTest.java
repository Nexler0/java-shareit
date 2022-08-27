package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookingControllerTest {


    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private User user;
    private User user2;
    private Booking booking;
    private BookingDtoIn bookingDtoIn;

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());

        user = new User(1L, "Jef", "user@123.ru");
        userService.addUser(user);
        user2 = new User(2L, "Jass", "Jass@mail.ru");
        userService.addUser(user2);

        Item item = new Item();
        item.setId(1L);
        item.setName("Screw");
        item.setDescription("Regular Screw");
        item.setAvailable(true);
        item.setUser(user);
        itemService.addNewItem(user.getId(), item);

        booking = new Booking();
        booking.setBooker(user2);
        booking.setItem(item);
        booking.setStartDate(LocalDateTime.now().withNano(0));
        booking.setEndDate(LocalDateTime.now().plusDays(1).withNano(0));
        booking.setId(1L);
        booking.setStatus(Status.WAITING);

        bookingDtoIn = new BookingDtoIn();
        bookingDtoIn.setBookerId(booking.getBooker().getId());
        bookingDtoIn.setItemId(item.getId());
        bookingDtoIn.setStart(booking.getStartDate());
        bookingDtoIn.setEnd(booking.getEndDate());
        bookingDtoIn.setStatus(bookingDtoIn.getStatus());
        bookingDtoIn.setId(booking.getId());
    }

    @Test
    void createBookingTest() throws Exception {
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", user2.getId())
                        .content(mapper.writeValueAsString(bookingDtoIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(booking.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(booking.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.status", equalTo(booking.getStatus().toString())));
    }

    @Test
    void getAllBookingTest() throws Exception {
        bookingService.createBooking(user2.getId(), booking);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", user2.getId())
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(booking.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(booking.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].status", equalTo(booking.getStatus().toString())));

    }

    @Test
    void getAllOwnerBookingTest() throws Exception {
        bookingService.createBooking(user2.getId(), booking);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", user.getId())
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(booking.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(booking.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].status", equalTo(booking.getStatus().toString())));

    }

    @Test
    void getBookingByIdTest() throws Exception {
        bookingService.createBooking(user2.getId(), booking);

        mockMvc.perform(get("/bookings/{id}", booking.getId())
                        .header("X-Sharer-User-Id", user.getId())
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(booking.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(booking.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.status", equalTo(booking.getStatus().toString())));

    }

    @Test
    void setApproveBookingByOwner() throws Exception {
        bookingService.createBooking(user2.getId(), booking);

        mockMvc.perform(patch("/bookings/{id}", booking.getId())
                        .header("X-Sharer-User-Id", user.getId())
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(booking.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(booking.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.status", equalTo("APPROVED")));
    }
}
