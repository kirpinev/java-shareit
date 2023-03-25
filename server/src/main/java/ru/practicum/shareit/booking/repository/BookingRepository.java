package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b JOIN Item i ON b.item.id = i.id " +
            "WHERE b.booker.id = :bookerId AND i.id = :itemId AND b.status = 'APPROVED' AND b.end < :currentTime")
    List<Booking> getAllUserBookings(Long bookerId, Long itemId, LocalDateTime currentTime);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.item.id = i.id " +
            "WHERE b.booker.id = :bookerId " +
            "ORDER BY b.start DESC")
    List<Booking> getAllBookingsByBookerId(Long bookerId, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.item.id = i.id " +
            "WHERE b.booker.id = :bookerId AND :currentTime BETWEEN b.start AND b.end " +
            "ORDER BY b.start DESC")
    List<Booking> getAllCurrentBookingsByBookerId(Long bookerId, LocalDateTime currentTime, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.item.id = i.id " +
            "WHERE b.booker.id = :bookerId AND b.start > :currentTime " +
            "ORDER BY b.start DESC")
    List<Booking> getAllFutureBookingsByBookerId(Long bookerId, LocalDateTime currentTime, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.item.id = i.id " +
            "WHERE b.booker.id = :bookerId AND b.status = 'REJECTED' " +
            "ORDER BY b.start DESC")
    List<Booking> getAllRejectedBookingsByBookerId(Long bookerId, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.item.id = i.id " +
            "WHERE b.booker.id = :bookerId AND b.end < :currentTime " +
            "ORDER BY b.start DESC")
    List<Booking> getAllPastBookingsByBookerId(Long bookerId, LocalDateTime currentTime, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.item.id = i.id " +
            "WHERE b.booker.id = :bookerId AND b.status = 'WAITING' AND b.start > :currentTime " +
            "ORDER BY b.start DESC")
    List<Booking> getAllWaitingBookingsByBookerId(Long bookerId, LocalDateTime currentTime, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.item.id = i.id " +
            "WHERE i.owner.id = :ownerId " +
            "ORDER BY b.start DESC")
    List<Booking> getAllBookingsByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.item.id = i.id " +
            "WHERE i.owner.id = :ownerId AND :currentTime BETWEEN b.start AND b.end " +
            "ORDER BY b.start DESC")
    List<Booking> getAllCurrentBookingsByOwnerId(Long ownerId, LocalDateTime currentTime, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.item.id = i.id " +
            "WHERE i.owner.id = :ownerId AND b.status = 'WAITING' AND b.start > :currentTime " +
            "ORDER BY b.start DESC")
    List<Booking> getAllWaitingBookingsByOwnerId(Long ownerId, LocalDateTime currentTime, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.item.id = i.id " +
            "WHERE i.owner.id = :ownerId AND b.start > :currentTime " +
            "ORDER BY b.start DESC")
    List<Booking> getAllFutureBookingsByOwnerId(Long ownerId, LocalDateTime currentTime, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.item.id = i.id " +
            "WHERE i.owner.id = :ownerId AND b.status = 'REJECTED' " +
            "ORDER BY b.start DESC")
    List<Booking> getAllRejectedBookingsByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.item.id = i.id " +
            "WHERE i.owner.id = :ownerId AND b.end < :currentTime " +
            "ORDER BY b.start DESC")
    List<Booking> getAllPastBookingsByOwnerId(Long ownerId, LocalDateTime currentTime, Pageable pageable);

    @Query(value = "SELECT * FROM bookings b "
            + "WHERE b.item_id in :itemId AND b.start_date < :currentTime " +
            "ORDER BY b.end_date DESC",
            nativeQuery = true)
    List<Booking> getLastBooking(List<Long> itemId, LocalDateTime currentTime);

    @Query(value = "SELECT * FROM bookings b "
            + "WHERE b.item_id in :itemId AND b.start_date > :currentTime AND b.status != 'REJECTED' " +
            "ORDER BY b.start_date",
            nativeQuery = true)
    List<Booking> getNextBooking(List<Long> itemId, LocalDateTime currentTime);
}
