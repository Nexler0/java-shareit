package ru.practicum.shareit.booking;

import lombok.NonNull;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource(path = "bookings")
public interface BookingRepository extends JpaRepository<Booking, Long>, BookingRepositoryCustom {

    @Override
    @Query("select b from Booking b order by b.startDate desc")
    @NonNull
    List<Booking> findAll();

    @Override
    @Query("select b from Booking b order by b.startDate desc")
    @NonNull
    Page<Booking> findAll(@NonNull Pageable pageable);

    @Query("select b from Booking b where b.id = ?1")
    Booking getBookingById(Long bookingId);

    @Modifying
    @Query("update Booking b set b.status = ?1 where b.id= ?2")
    void setBookingInfoById(Status status, Long id);

    @Query("select b from Booking b where b.startDate < ?1 order by b.startDate ")
    Page<Booking> getAllBookingBeforeStartDate(LocalDateTime timeMoment, Pageable pageable);

    @Query("select b from Booking b where b.endDate < ?1 order by b.startDate desc")
    Page<Booking> getAllBookingBeforeEndDate(LocalDateTime timeMoment, Pageable pageable);

    @Query("select b from Booking b where b.startDate > ?1 order by b.startDate desc")
    Page<Booking> getAllBookingAfterStartDate(LocalDateTime timeMoment, Pageable pageable);

    @Query("select b from Booking b where b.item.user.id = ?1 order by b.startDate desc")
    Page<Booking> getBookingsByItemUserId(Long userId, Pageable pageable);

    @Query("select b from Booking b where b.item.user.id = ?1 and b.startDate < ?2 order by b.startDate desc")
    Page<Booking> getBookingsByItemUserIdBeforeStartDate(Long userId, LocalDateTime now, Pageable pageable);

    @Query("select b from Booking b where b.item.user.id = ?1 and b.endDate < ?2 order by b.startDate desc")
    Page<Booking> getBookingsByItemUserIdBeforeEndDate(Long userId, LocalDateTime now, Pageable pageable);

    @Query("select b from Booking b where b.item.user.id = ?1 and b.startDate > ?2 order by b.startDate desc")
    Page<Booking> getBookingsByItemUserIdAfterStartDate(Long userId, LocalDateTime now, Pageable pageable);

    @Query("select b from Booking b where b.item.id = ?1")
    List<Booking> getBookingsByItemId(Long id);

    @Query("select (count(b) > 0) from Booking b where b.booker.id = ?1")
    Boolean existsBookingByBookerId(Long id);
}
