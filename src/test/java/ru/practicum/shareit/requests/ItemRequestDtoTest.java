package ru.practicum.shareit.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@JsonTest
public class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;
    private ItemRequestDto itemRequest;

    @BeforeEach
    void setUp() {
        itemRequest = new ItemRequestDto();
        itemRequest.setId(1L);
        itemRequest.setRequesterId(1L);
        itemRequest.setDescription("Need something");
        itemRequest.setCreated(LocalDateTime.now().withNano(0));
    }

    @Test
    void serializeTest() throws IOException {
        JsonContent<ItemRequestDto> userJson = this.json.write(itemRequest);
        assertThat(userJson.getJson(),
                is("{\"id\":1,\"description\":\"Need something\",\"requesterId\":1," +
                        "\"created\":\"" + LocalDateTime.now().withNano(0) + "\",\"items\":null}"));
    }
}
