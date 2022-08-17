package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.booking.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings_short")
@Getter
@Setter
@NoArgsConstructor
public class BookingShort {

    @Id
    private Long id;
    private Long bookerId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long itemId;
    @Transient
    @Enumerated(EnumType.STRING)
    private Status status;
}
