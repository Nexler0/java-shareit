package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.practicum.shareit.booking.model.BookingShort;

@RepositoryRestResource(path = "bookings_short")
public interface BookingShortRepository extends JpaRepository<BookingShort, Long>, BookingShortRepositoryCustom {
}
