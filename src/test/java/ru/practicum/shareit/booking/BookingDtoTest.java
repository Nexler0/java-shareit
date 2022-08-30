package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@JsonTest
public class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDtoOut> json;
    private BookingDtoOut bookingDtoOut;

    @BeforeEach
    void setUp() {
        Item item = new Item();
        item.setName("screw");
        item.setDescription("cool screw");
        item.setUser(new User(1L, "jef", "jef@mail.ru"));
        item.setId(1L);

        bookingDtoOut = BookingDtoOut.builder()
                .id(1L)
                .booker(new User(2L, "jess", "jess@mail.ru"))
                .item(item)
                .start(LocalDateTime.now().withNano(0))
                .end(LocalDateTime.now().plusDays(1).withNano(0))
                .status(Status.WAITING)
                .build();
    }

    @Test
    void serializeTest() throws IOException {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBooker(new User(1L, "user", "user@mail.ru"));
        booking.setStartDate(LocalDateTime.now().withNano(0));
        booking.setEndDate(LocalDateTime.now().plusDays(1).withNano(0));
        booking.setStatus(Status.APPROVED);
        JsonContent<BookingDtoOut> userJson = this.json.write(bookingDtoOut);
        assertThat(userJson.getJson(),
                is("{\"id\":1,\"start\":\"" + LocalDateTime.now().withNano(0) + "\",\"end\":\""
                        + LocalDateTime.now().plusDays(1).withNano(0) + "\",\"item\":{\"id\":1,\"" +
                        "user\":{\"id\":1,\"name\":\"jef\",\"email\":\"jef@mail.ru\"},\"" +
                        "name\":\"screw\",\"description\":\"cool screw\",\"available\":null,\"" +
                        "itemRequest\":null,\"lastBooking\":null,\"nextBooking\":null,\"comments\":null}," +
                        "\"booker\":{\"id\":2,\"name\":\"jess\",\"email\":\"jess@mail.ru\"}," +
                        "\"status\":\"WAITING\"}"));
    }
}
