package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@RepositoryRestResource(path = "items")
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom{

    @Query("select i from Item i where i.user.id = ?1")
    List<Item> findItemByUserId(Long userId);

    @Query("select i from Item i where i.id = ?1")
    Item getItemById(Long itemId);

    Item getItemByItemRequest(Item item);

    @Modifying
    @Query("update Item i set i.name = ?1, i.description = ?2, i.available = ?3, i.itemRequest = ?4" +
            " where i.id = ?5")
    public void setItemInfoById(String name, String description, Boolean available, Long requestId, Long id);

    Boolean existsItemById(Long id);

}
