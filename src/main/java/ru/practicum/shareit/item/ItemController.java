package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> get(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getItemsByUserId(userId).stream().map(ItemMapper::toDto).collect(Collectors.toList());
    }

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") int userId,
                       @Valid @RequestBody ItemDto itemDto) {
        return ItemMapper.toDto(itemService.addNewItem(userId, ItemMapper.toItem(itemDto)));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int itemId,
                              @Valid @RequestBody ItemDto itemDto) {
        return ItemMapper.toDto(itemService.updateItem(userId, itemId, ItemMapper.toItem(itemDto)));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId) {
        return ItemMapper.toDto(itemService.getItemById(itemId));
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") int userId,
                           @PathVariable int itemId) {
        itemService.deleteItem(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItemByRequest(@RequestParam(name = "text") String text) {
        return itemService.findItemByRequest(text).stream().map(ItemMapper::toDto).collect(Collectors.toList());
    }
}