package ru.practicum.shareit.item;

import org.springframework.context.annotation.Lazy;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;


public class ItemRepositoryImpl implements ItemRepositoryCustom{

    private static int id = 0;
    private final HashMap<Integer, Item> itemHashMap = new HashMap<>();
    private final ItemRepository itemRepository;

    public ItemRepositoryImpl(@Lazy ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> findAllByUserIdWithUrlState(Long userId) {
        return null;
    }

//    @Override
//    public Item addItem(Item item) {
////        if (item.getId() == 0) {
////            id++;
////            item.setId(id);
////        }
////        itemHashMap.put(item.getId(), item);
//        return item;
//    }
//
//    @Override
//    public void deleteItem(int itemId) {
//        itemHashMap.remove(itemId);
//    }
//
//    @Override
//    public List<Item> getAllItems() {
//        return new ArrayList<>(itemHashMap.values());
//    }
//
//    @Override
//    public Item getItemById(int itemId) {
//        return itemHashMap.get(itemId);
//    }
//
//    @Override
//    public List<Item> findItemByUserId(int userId) {
//        return itemHashMap.values().stream().filter(item -> item.getUserId() == userId).collect(Collectors.toList());
//    }


}
