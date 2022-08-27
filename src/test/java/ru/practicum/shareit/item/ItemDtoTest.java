package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
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
        JsonContent<ItemDtoOut> userJson = this.json.write(itemDtoOut);
        assertThat(userJson.getJson(),
                is("{\"id\":1,\"userId\":1,\"requestId\":null,\"name\":\"screw\",\"description\":\"new screw\"" +
                        ",\"available\":true,\"lastBooking\":null,\"nextBooking\":null,\"comments\":null}"));
    }
}