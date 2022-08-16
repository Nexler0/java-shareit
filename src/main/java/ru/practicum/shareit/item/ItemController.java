package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.CommentService;
import ru.practicum.shareit.comment.model.CommentShort;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;
    private final ItemMapper itemMapper;

    @GetMapping
    public List<ItemDtoOut> get(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemsByUserId(userId).stream().map(itemMapper::toDto)
                .sorted((o1, o2) -> o1.getId().compareTo(o2.getId())).collect(Collectors.toList());
    }

    @PostMapping
    public ItemDtoOut add(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @Valid @RequestBody ItemDtoIn itemDtoIn) {
        itemDtoIn.setUserId(userId);
        Item item = itemMapper.toItem(itemDtoIn);
        return itemMapper.toDto(itemService.addNewItem(userId, item));
    }

    @PatchMapping("/{itemId}")
    public ItemDtoOut updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId,
                                 @Valid @RequestBody ItemDtoIn itemDtoIn) {
        itemDtoIn.setUserId(userId);
        Item item = itemMapper.toItem(itemDtoIn);
        return itemMapper.toDto(itemService.updateItem(userId, itemId, item));
    }

    @GetMapping("/{itemId}")
    public ItemDtoOut getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long itemId) {
        return itemMapper.toDto(itemService.getItemById(userId, itemId));
    }

    @PostMapping("/{itemId}/comment")
    public CommentShort postCommentToItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long itemId,
                                          @Valid @RequestBody Comment comment) {
        return commentService.addComment(userId, itemId, comment);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable Long itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDtoOut> findItemByRequest(@RequestParam(name = "text") String text) {
        return itemService.findItemByRequest(text).stream().map(itemMapper::toDto).collect(Collectors.toList());
    }
}