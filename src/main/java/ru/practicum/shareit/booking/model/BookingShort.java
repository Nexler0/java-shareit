package ru.practicum.shareit.booking.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BookingShort {

    private Long id;
    private Long bookerId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long itemId;
    private Status status;

}
