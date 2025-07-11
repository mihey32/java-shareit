package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.enums.Statuses;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findAllByBookerId(Long bookerId);

    Collection<Booking> findAllByBookerIdAndStatus(Long bookerId, Statuses status);

    @Query("select b " +
            "from Booking as b " +
            "where b.booker.id = ?1 " +
            "and CURRENT_TIMESTAMP BETWEEN b.start and b.end")
    Collection<Booking> findAllCurrentBookingByBookerId(Long bookerId);

    @Query("select b " +
            "from Booking as b " +
            "where b.booker.id = ?1 " +
            "and CURRENT_TIMESTAMP > b.end")
    Collection<Booking> findAllPastBookingByBookerId(Long bookerId);

    @Query("select b " +
            "from Booking as b " +
            "where b.booker.id = ?1 " +
            "and CURRENT_TIMESTAMP < b.start")
    Collection<Booking> findAllFutureBookingByBookerId(Long bookerId);


    @Query("select b " +
            "from Booking as b " +
            "where b.item.user.id = ?1")
    Collection<Booking> findAllByOwnerId(Long ownerId);

    @Query("select b " +
            "from Booking as b " +
            "where b.item.user.id = ?1 " +
            "and b.status = ?2")
    Collection<Booking> findAllByOwnerIdAndStatus(Long ownerId, Statuses status);

    @Query("select b " +
            "from Booking as b " +
            "where b.item.user.id = ?1 " +
            "and CURRENT_TIMESTAMP BETWEEN b.start and b.end")
    Collection<Booking> findAllCurrentBookingByOwnerId(Long ownerId);

    @Query("select b " +
            "from Booking as b " +
            "where b.item.user.id = ?1 " +
            "and CURRENT_TIMESTAMP > b.end")
    Collection<Booking> findAllPastBookingByOwnerId(Long ownerId);

    @Query("select b " +
            "from Booking as b " +
            "where b.item.user.id = ?1 " +
            "and CURRENT_TIMESTAMP < b.start")
    Collection<Booking> findAllFutureBookingByOwnerId(Long ownerId);

    Boolean existsByBookerIdAndItemIdAndEndBefore(Long bookerId, Long itemId, LocalDateTime currentTimeStamp);

    @Query("select b.start " +
            "from Booking as b " +
            "where b.item.id = ?1 " +
            "and b.status = ?2 " +
            "and ?3 < b.start")
    List<LocalDateTime> findNextBookingStartByItemId(Long itemId, Statuses status, LocalDateTime currentTimeStamp);

    @Query("select b.end " +
            "from Booking as b " +
            "where b.item.id = ?1 " +
            "and b.status = ?2 " +
            "and ?3 > b.end ")
    List<LocalDateTime> findLastBookingEndByItemId(Long itemId, Statuses status, LocalDateTime currentTimeStamp);

    @Query("select b " +
            "from Booking as b " +
            "where b.item.id in (?1) " +
            "and b.status = ?2 " +
            "and ?3 > b.end " +
            "order by b.end DESC")
    List<Booking> findByItemInAndEndBefore(List<Long> ids, Statuses status, LocalDateTime currentTimeStamp);

    @Query("select b " +
            "from Booking as b " +
            "where b.item.id in (?1) " +
            "and b.status = ?2 " +
            "and ?3 < b.start " +
            "order by b.end ASC")
    List<Booking> findByItemInAndStartAfter(List<Long> ids, Statuses status, LocalDateTime currentTimeStamp);
}