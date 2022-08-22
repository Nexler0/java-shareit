package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@RepositoryRestResource(path = "items")
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    @Query("select i from Item i where i.user.id = ?1")
    List<Item> findItemByUserId(Long userId);

    @Query("select i from Item i where i.id = ?1")
    Item getItemById(Long itemId);

    @Query("select i from Item i where i.itemRequest.id = ?1")
    Page<Item> getAllByItemRequestId(Long requestId, Pageable pageable);

    @Query("select i from Item i where lower(i.name) like lower(concat('%', ?1, '%'))" +
            "or lower(i.description) like lower(concat('%', ?1, '%')) and i.available = true")
    List<Item> search(String item);

    @Query("select (count(i) > 0) from Item i where i.id = ?1")
    Boolean existsItemById(Long id);
}
