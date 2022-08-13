package ru.practicum.shareit.booking;

import lombok.NonNull;
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

    @Query("select b from Booking b where b.id = ?1")
    Booking getBookingById(Long bookingId);

    @Modifying
    @Query("update Booking b set b.status = ?1 where b.id= ?2")
    void setBookingInfoById(Status status, Long id);

//    @Query("select b, booker.id, item.id, item.name from Booking as b where b.id = ?1")
//    Booking getBookingByIdWithJoin(Long id);

    @Query("select b from Booking b where b.startDate < ?1 order by b.startDate DESC")
    List<Booking> getAllByStartDateBeforeOrderByStartDateDesc(LocalDateTime timeMoment);

    @Query("select b from Booking b where b.endDate < ?1 order by b.startDate DESC")
    List<Booking> getAllByEndDateBeforeOrderByStartDateDesc(LocalDateTime timeMoment);

    @Query("select b from Booking b where b.startDate > ?1 order by b.startDate DESC")
    List<Booking> getAllByStartDateAfterOrderByStartDateDesc(LocalDateTime timeMoment);

    @Query("select b from Booking b where b.status like concat('%', ?1, '%') order by b.startDate Desc")
    List<Booking> getAllByStatusContainsOrderByStartDateDesc(String status);

    @Query("select b from Booking b where b.item.user.id = ?1 order by b.startDate DESC")
    List<Booking> getBookingByItemUserIdOrderByStartDateDesc(Long userId);

    @Query("select b from Booking b where b.item.user.id = ?1 and b.startDate < ?2 order by b.startDate Desc")
    List<Booking> getBookingByItemUserIdAndStartDateBeforeOrderByStartDateDesc(Long userId, LocalDateTime now);

    @Query("select b from Booking b where b.item.user.id = ?1 and b.endDate < ?2 order by b.startDate Desc")
    List<Booking> getBookingByItemUserIdAndEndDateBeforeOrderByStartDateDesc(Long userId, LocalDateTime now);

    @Query("select b from Booking b where b.item.user.id = ?1 and b.startDate > ?2 order by b.startDate Desc")
    List<Booking> getBookingByItemUserIdAndStartDateAfterOrderByStartDateDesc(Long userId, LocalDateTime now);

    @Query("select b from Booking b " +
            "where b.item.user.id = ?1 and b.status like concat('%', ?2, '%') " +
            "order by b.startDate Desc")
    List<Booking> getBookingByItemUserIdAndStatusContainsOrderByStartDateDesc(String waiting);
}