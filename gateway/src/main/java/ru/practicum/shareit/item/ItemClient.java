package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.comment.CommentShort;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDtoIn;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getAllItems(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> postItem(Long userId, ItemDtoIn itemDtoIn) {
        isValidateItem(itemDtoIn);
        return post("", userId, itemDtoIn);
    }

    public ResponseEntity<Object> updateItem(Long userId, Long itemId, ItemDtoIn itemDtoIn) {
        return patch("/" + itemId, userId, itemDtoIn);
    }

    public ResponseEntity<Object> getIteById(Long userId, Long itemId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> postCommentToItem(Long userId, Long itemId, CommentShort commentShort) {
        if (commentShort.getText() == null || commentShort.getText().equals("")
                || commentShort.getText().length() == 0) {
            throw new ValidationException("Ошибка валидации");
        }
        return post("/" + itemId + "/comment", userId, commentShort);
    }

    public ResponseEntity<Object> deleteItemById(Long userId, Long itemId) {
        return delete("/" + itemId, userId);
    }

    public ResponseEntity<Object> findItemByRequest(Long userId, String text) {
        return get("/search?text=" + text, userId);
    }

    private void isValidateItem(ItemDtoIn itemDtoIn) {
        if (itemDtoIn.getAvailable() == null
                && itemDtoIn.getName() == null && !itemDtoIn.getName().isEmpty()
                && itemDtoIn.getDescription() == null && itemDtoIn.getDescription().isEmpty()) {
            throw new ValidationException("Ошибка валидации Item");
        } else if (itemDtoIn.getAvailable() == null
                || itemDtoIn.getName() == null || itemDtoIn.getName().isEmpty()
                || itemDtoIn.getDescription() == null || itemDtoIn.getDescription().isEmpty()) {
            throw new ValidationException("Входные условия item");
        }
    }
}
