package ru.practicum.shareit.booking.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingShort that = (BookingShort) o;
        return Objects.equals(bookerId, that.bookerId) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(itemId, that.itemId) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookerId, startDate, endDate, itemId, status);
    }
}
