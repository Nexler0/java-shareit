package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.dto.ItemMapper;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @GetMapping
    public List<ItemDtoOut> get(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemsByUserId(userId).stream().map(itemMapper::toDto).sorted(new Comparator<ItemDtoOut>() {
            @Override
            public int compare(ItemDtoOut o1, ItemDtoOut o2) {
                return o1.getId().compareTo(o2.getId());
            }
        }).collect(Collectors.toList());
    }

    @PostMapping
    public ItemDtoOut add(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @Valid @RequestBody ItemDtoIn itemDtoIn) {
        itemDtoIn.setUserId(userId);
        return itemMapper.toDto(itemService.addNewItem(userId, itemMapper.toItem(itemDtoIn)));
    }

    @PatchMapping("/{itemId}")
    public ItemDtoOut updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId,
                                 @Valid @RequestBody ItemDtoIn itemDtoIn) {
        itemDtoIn.setUserId(userId);
        return itemMapper.toDto(itemService.updateItem(userId, itemId, itemMapper.toItem(itemDtoIn)));
    }

    @GetMapping("/{itemId}")
    public ItemDtoOut getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long itemId) {
        return itemMapper.toDto(itemService.getItemById(userId, itemId));
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