package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private final ItemRequestMapper itemRequestMapper;

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestBody ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(userId, itemRequestDto);
        return itemRequestMapper.toDto(itemRequestService.addRequestFromUser(userId, itemRequest));
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(name = "from", defaultValue = "0",
                                                       required = false) Integer from,
                                               @RequestParam(name = "size", defaultValue = "10",
                                                       required = false) Integer size) {

        return itemRequestService.getAllRequests(userId, from, size).stream().map(itemRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequestsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getAllRequestsByOwner(userId).stream().map(itemRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long requestId,
                                             @RequestParam(name = "from", defaultValue = "0",
                                             required = false) Integer from,
                                             @RequestParam(name = "size", defaultValue = "10",
                                             required = false) Integer size) {
        return itemRequestMapper.toDto(itemRequestService.getItemRequestsById(userId, requestId));
    }
}
