package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepositoryCustom {
    List<Item> findAllByUserId(Long userId);
}
