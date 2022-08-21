package ru.practicum.shareit.requests;

import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequest addRequestFromUser(Long userId, ItemRequest itemRequest);

    List<ItemRequest> getAllRequestsByOwner(Long userId);

    List<ItemRequest> getAllRequests(Long userId, int from, int size);

    ItemRequest getItemRequestsById(Long userId, Long requestId);
}
