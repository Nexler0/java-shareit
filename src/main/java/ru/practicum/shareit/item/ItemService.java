package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item addNewItem(Long userId, Item item);

    void deleteItem(Long userId, Long itemId);

    List<Item> getItemsByUserId(Long userId);

    List<Item> getAllItems();

    Item updateItem(Long userId, Long itemId, Item item);

    Item getItemById(Long itemId);

    List<Item> findItemByRequest(String text);
}
