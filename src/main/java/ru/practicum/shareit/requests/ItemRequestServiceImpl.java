package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequest addRequestFromUser(Long userId, ItemRequest itemRequest) {
        if (itemRequest.getDescription() == null || itemRequest.getDescription().isEmpty()) {
            throw new ValidationException("Пустой запрос");
        }
        itemRequest.setCreated(LocalDateTime.now().withNano(0));
        if (userRepository.existsUserById(userId)) {
            return itemRequestRepository.save(itemRequest);
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public List<ItemRequest> getAllRequestsByOwner(Long userId) {
        if (!userRepository.existsUserById(userId)) {
            throw new NotFoundException("Пользовательно не найден");
        }
        return itemRequestRepository.findItemRequestByRequesterId(userId);

    }

    @Override
    public List<ItemRequest> getAllRequests(Long userId, int from, int size) {
        if (from < 0 || size <= 0) {
            throw new ValidationException("Параметр from или size задан неверно");
        }
        Page<ItemRequest> result = itemRequestRepository.findAll(PageRequest.of(from, size));
        if (result.isEmpty()) {
            return new ArrayList<>();
        } else {
            return result.stream().filter(itemRequest -> !itemRequest.getRequester().getId().equals(userId))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public ItemRequest getItemRequestsById(Long userId, Long requestId) {
        if (!userRepository.existsUserById(userId) || !itemRequestRepository.existsById(requestId)
                || requestId > itemRequestRepository.count()) {
            throw new NotFoundException("Неверные условия поиска");
        }
        return itemRequestRepository.getItemRequestById(requestId);
    }
}
