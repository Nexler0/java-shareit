package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom,
        QuerydslPredicateExecutor<Item> {

    Item addItem(Item item);

    void deleteItem(int itemId);

    List<Item> getAllItems();

    List<Item> findItemByUserId(int userId);

    Item getItemById(int itemId);
}
