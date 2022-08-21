package ru.practicum.shareit.requests.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemRequestMapper {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemRequest toItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        ItemRequest result = new ItemRequest();
        result.setId(itemRequestDto.getId());
        result.setDescription(itemRequestDto.getDescription());
        result.setRequester(userRepository.getUserById(userId));
        result.setCreated(itemRequestDto.getCreated());
        result.setItems(itemRequestDto.getItems());
        return result;
    }

    public ItemRequestDto toDto(ItemRequest itemRequest) {
        ItemRequestDto result = ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requesterId(itemRequest.getRequester().getId())
                .created(itemRequest.getCreated())
                .build();
        result.setItems(itemRepository.getAllByItemRequestId(itemRequest.getId(), PageRequest.of(0, 100))
                .stream().map(itemMapper::toShort).collect(Collectors.toList()));
        return result;
    }
}
