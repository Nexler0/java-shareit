package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long itemId;
    private Long bookerId;
    private Status status;
}
