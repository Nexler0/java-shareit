package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Call method: ItemRequestController.addRequest UserId: {}, Body: {}",
                userId, itemRequestDto.toString());
        return itemRequestClient.addRequest(userId, itemRequestDto);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PositiveOrZero
                                                 @RequestParam(name = "from", defaultValue = "0",
                                                         required = false) Integer from,
                                                 @Positive
                                                 @RequestParam(name = "size", defaultValue = "10",
                                                         required = false) Integer size) {
        log.info("Call method: ItemRequestController.getAllRequests UserId: {}, from: {}, size: {}",
                userId, from, size);
        return itemRequestClient.getAllRequests(userId, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.getItemRequestByOwner(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @PathVariable Long requestId,
                                                     @PositiveOrZero
                                                     @RequestParam(name = "from", defaultValue = "0",
                                                             required = false) Integer from,
                                                     @Positive
                                                     @RequestParam(name = "size", defaultValue = "10",
                                                             required = false) Integer size) {
        return itemRequestClient.getItemRequestById(userId, requestId, from, size);
    }
}
