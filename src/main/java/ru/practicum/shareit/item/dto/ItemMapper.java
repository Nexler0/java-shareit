package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMapper {
    
    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .userId(item.getUser().getId())
                .requestId(null)
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setUser(null);
        item.setItemRequest(null);
        return item;
    }

    public static List<ItemDto> mapToItemDto(Iterable<Item> items) {
        List <ItemDto> result = new ArrayList<>();
        for (Item item : items){
            result.add(ItemMapper.toDto(item));
        }
        return result;
    }
}
