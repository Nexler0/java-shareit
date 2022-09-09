package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class BookingShort {

    @EqualsAndHashCode.Exclude
    private Long id;
    private Long bookerId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long itemId;
    private Status status;

}
