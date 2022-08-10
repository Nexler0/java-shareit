package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
            item.setUser(userRepository.getReferenceById(userId));
            return itemRepository.save(item);
        } else if (userRepository.existsUserById(userId)) {
            throw new NotFoundException("пользователь не найден");
        } else if (item.getAvailable() == null
                && item.getName() == null && item.getName().isEmpty()
                && item.getDescription() == null && item.getDescription().isEmpty()) {
            throw new ValidationException("Входные условия item");
        } else {
            throw new NotFoundException("Пользователь не найден");
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
        List<Item> result = new ArrayList<>();
        if (text != null && !text.isEmpty() && !text.isBlank()) {
            result = itemRepository.findAll().stream().filter(
                            item -> (item.getDescription().toLowerCase(Locale.ROOT)
                                    .contains(text.toLowerCase(Locale.ROOT))
                                    || item.getName().toLowerCase(Locale.ROOT)
                                    .contains(text.toLowerCase(Locale.ROOT)))
                                    && item.getAvailable())
                    .collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> getItemsByUserId(Long userId) {
        if (userId == 0) {
            return itemRepository.findAll();
        } else {
            return itemRepository.findItemByUserId(userId);
        }
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.getReferenceById(itemId);
    }


    @Override
    public Item updateItem(Long userId, Long itemId, Item item) {
        Item oldItem = itemRepository.getItemById(itemId);

        if (userRepository.findById(userId).isEmpty() &&
                itemRepository.getItemById(itemId) != null && userId.equals(oldItem.getUser().getId())) {
            if (item.getName() != null && !item.getName().isEmpty()) {
                oldItem.setName(item.getName());
                itemRepository.deleteById(itemId);
            }
            if (item.getDescription() != null && !item.getDescription().isEmpty()) {
                oldItem.setDescription(item.getDescription());
                itemRepository.deleteById(itemId);
            }
            if (item.getAvailable() != oldItem.getAvailable() && item.getAvailable() != null) {
                oldItem.setAvailable(item.getAvailable());
                itemRepository.deleteById(itemId);
            }
            return itemRepository.save(oldItem);
        }
        throw new NotFoundException("Итернал");
    }
}
