package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@RepositoryRestResource(path = "items")
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom{

    List<Item> findItemByUserId(int userId);

    Item getItemById(int itemId);
}
