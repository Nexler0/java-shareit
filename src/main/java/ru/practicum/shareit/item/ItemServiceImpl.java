package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Override
    public Item addNewItem(Long userId, Item item) {
        if (userRepository.existsUserById(userId)
                && item.getAvailable() != null
                && item.getName() != null && !item.getName().isEmpty()
                && item.getDescription() != null && !item.getDescription().isEmpty()) {
            return itemRepository.save(item);
        } else if (!userRepository.existsUserById(userId)) {
            throw new NotFoundException("Пользователь не найден");
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
        return itemRepository.search(text).stream().map(this::setBookingToItem).collect(Collectors.toList());
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll().stream().map(this::setBookingToItem).collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemsByUserId(Long userId) {
        if (userId == null || userId == 0) {
            return itemRepository.findAll().stream().map(this::setBookingToItem).collect(Collectors.toList());
        } else {
            return itemRepository.findItemByUserId(userId).stream().map(this::setBookingToItem)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Item getItemById(Long userid, Long itemId) {
        if (itemId <= itemRepository.count()) {
            Item item = itemRepository.getItemById(itemId);
            if (userid.equals(item.getUser().getId())) {
                return setBookingToItem(item);
            } else {
                item.setLastBooking(null);
                item.setNextBooking(null);
                return item;
            }
        } else {
            throw new NotFoundException("Предмет не найден");
        }
    }

    private Item setBookingToItem(Item item) {
        List<Booking> bookings = bookingRepository.getBookingsByItemId(item.getId()).stream()
                .sorted((o1, o2) -> (o2.getStartDate().compareTo(o1.getStartDate())))
                .collect(Collectors.toList());
        if (bookings.size() == 1) {
            item.setLastBooking(BookingMapper.toBookingShort(bookings.get(0)));
        } else if (bookings.size() > 1) {
            item.setLastBooking(BookingMapper.toBookingShort(bookings.get(bookings.size() - 1)));
            item.setNextBooking(BookingMapper.toBookingShort(bookings.get(bookings.size() - 2)));
        }
        return item;
    }

    @Transactional
    @Override
    public Item updateItem(Long userId, Long itemId, Item item) {
        Item oldItem = itemRepository.getItemById(itemId);
        boolean isUpdate = false;

        if (userRepository.existsUserById(userId)
                && itemRepository.existsItemById(itemId)
                && userId.equals(oldItem.getUser().getId())) {
            if (item.getName() != null && !item.getName().isEmpty()) {
                oldItem.setName(item.getName());
                isUpdate = true;
            }
            if (item.getDescription() != null && !item.getDescription().isEmpty()) {
                oldItem.setDescription(item.getDescription());
                isUpdate = true;
            }
            if (item.getAvailable() != oldItem.getAvailable() && item.getAvailable() != null) {
                oldItem.setAvailable(item.getAvailable());
                isUpdate = true;
            }
            if (isUpdate) {
                return oldItem;
            }
        }
        throw new NotFoundException("Ошибка входных параметров");
    }
}
