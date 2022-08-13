package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item addNewItem(Long userId, Item item) {
        if (userRepository.existsUserById(userId)
                && item.getAvailable() != null
                && item.getName() != null && !item.getName().isEmpty()
                && item.getDescription() != null && !item.getDescription().isEmpty()) {
            item.setUser(userRepository.getUserById(userId));
            // TODO item.getItemRequest()
            return itemRepository.save(item);
        } else if (!userRepository.existsUserById(userId)) {
            throw new NotFoundException("пользователь не найден");
        } else if (item.getAvailable() == null
                || item.getName() == null || item.getName().isEmpty()
                || item.getDescription() == null || item.getDescription().isEmpty()) {
            throw new ValidationException("Входные условия item");
        } else {
            throw new ValidationException("Ошибка валидации Item");
        }
    }

    @Override
    public void deleteItem(Long userId, Long itemId) {
        if (itemRepository.getItemById(itemId).getUser().getId().equals(userId)) {
            itemRepository.deleteById(itemId);
        } else {
            throw new AccessException("Доступ запрещен");
        }
    }

    @Override
    public List<Item> findItemByRequest(String text) {
        if (text == null || text.isEmpty() || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text);
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> getItemsByUserId(Long userId) {
        if (userId == null || userId == 0) {
            return itemRepository.findAll();
        } else {
            return itemRepository.findItemByUserId(userId);
        }
    }

    @Override
    public Item getItemById(Long itemId) {
        if (itemId <= itemRepository.count()) {
            return itemRepository.getItemById(itemId);
        } else {
            throw new NotFoundException("Item not found");
        }
    }

    @Transactional
    @Override
    public Item updateItem(Long userId, Long itemId, Item item) {
        Item oldItem = itemRepository.getItemById(itemId);
        boolean is_update = false;
        Long requestId = null;

        if (userRepository.existsUserById(userId)
                && itemRepository.existsItemById(itemId)
                && userId.equals(oldItem.getUser().getId())) {
            if (item.getName() != null && !item.getName().isEmpty()) {
                oldItem.setName(item.getName());
                is_update = true;
            }
            if (item.getDescription() != null && !item.getDescription().isEmpty()) {
                oldItem.setDescription(item.getDescription());
                is_update = true;
            }
            if (item.getAvailable() != oldItem.getAvailable() && item.getAvailable() != null) {
                oldItem.setAvailable(item.getAvailable());
                is_update = true;
            }
            if (oldItem.getItemRequest() != null) {
                requestId = oldItem.getItemRequest().getId();
            }
            if (is_update) {
                itemRepository.setItemInfoById(oldItem.getName(), oldItem.getDescription(),
                        oldItem.getAvailable(), requestId, oldItem.getId());
                return oldItem;
            }
        }
        throw new NotFoundException("Итернал");
    }
}
