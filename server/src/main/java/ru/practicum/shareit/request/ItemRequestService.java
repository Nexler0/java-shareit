package ru.practicum.shareit.request;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequest addRequestFromUser(Long userId, ItemRequest itemRequest);

    List<ItemRequest> getAllRequestsByOwner(Long userId);

    List<ItemRequest> getAllRequests(Long userId, Integer from, Integer size);

    ItemRequest getItemRequestsById(Long userId, Long requestId);
}
