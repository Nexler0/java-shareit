package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@JsonTest
public class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDtoOut> json;
    private ItemDtoOut itemDtoOut;

    @BeforeEach
    void setUp() {
        itemDtoOut = ItemDtoOut.builder()
                .id(1L)
                .name("screw")
                .description("new screw")
                .available(true)
                .userId(1L)
                .build();
    }

    @Test
    void serializeTest() throws IOException {
        Item item = new Item();
        item.setId(1L);
        item.setName("Screw");
        item.setDescription("New screw");
        item.setUser(new User(1L, "User", "user@mail.ru"));
        Item item2 = new Item();
        item2.setId(1L);
        item2.setName("Screw");
        item2.setDescription("New screw");
        item2.setUser(new User(1L, "User", "user@mail.ru"));
        assertThat(item, equalTo(item2));
        JsonContent<ItemDtoOut> userJson = this.json.write(itemDtoOut);

        assertThat(userJson.getJson(),
                is("{\"id\":1,\"userId\":1,\"requestId\":null,\"name\":\"screw\",\"description\":" +
                        "\"new screw\",\"available\":true,\"lastBooking\":null,\"nextBooking\":null," +
                        "\"comments\":null}"));
    }
}