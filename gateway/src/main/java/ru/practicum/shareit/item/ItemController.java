package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentShort;
import ru.practicum.shareit.item.dto.ItemDtoIn;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAllItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Call method: ItemController.getAllItems");
        return itemClient.getAllItems(userId);
    }

    @PostMapping
    public ResponseEntity<Object> postItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid ItemDtoIn itemDtoIn) {
        log.info("Call method: ItemController.postItem UserId: {}, Body: {}", userId, itemDtoIn.toString());
        return itemClient.postItem(userId, itemDtoIn);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long itemId,
                                             @Valid @RequestBody ItemDtoIn itemDtoIn) {
        log.info("Call method: ItemController.updateItem UserId: {}, itemId: {}, Body: {}",
                userId, itemId, itemDtoIn.toString());
        return itemClient.updateItem(userId, itemId, itemDtoIn);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PathVariable Long itemId) {
        log.info("Call method: ItemController.getItemById UserId: {}, itemId: {}", userId, itemId);
        return itemClient.getIteById(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postCommentToItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @PathVariable Long itemId,
                                                    @Valid @RequestBody CommentShort commentShort) {
        log.info("Call method: ItemController.postCommentToItem UserId: {}, itemId: {}, Body: {}",
                userId, itemId, commentShort.toString());
        return itemClient.postCommentToItem(userId, itemId, commentShort);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long itemId) {
        log.info("Call method: ItemController.deleteItemById UserId: {}, itemId: {}",
                userId, itemId);
        return itemClient.deleteItemById(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItemByRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @RequestParam(name = "text") String text) {
        log.info("Call method: ItemController.findItemByRequest UserId: {}, Text: {}", userId, text);
        return itemClient.findItemByRequest(userId, text);
    }
}
