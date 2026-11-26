package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingStorage extends JpaRepository<Booking, Long> {

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByBookerIdAndStartBeforeAndEndAfterAndStatusOrderByStartDesc(Long bookerId,
                                                                                   LocalDateTime now1,
                                                                                   LocalDateTime now2,
                                                                                   BookingStatus status);

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByBookerIdAndEndBeforeAndStatusOrderByStartDesc(Long bookerId, LocalDateTime now,
                                                                      BookingStatus bookingStatus);

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByBookerIdAndStartAfterAndStatusOrderByStartDesc(Long bookerId, LocalDateTime now,
                                                                       BookingStatus bookingStatus);

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByBookerIdAndStatusAndEndAfterOrderByStartDesc(Long bookerId,
                                                                     BookingStatus status, LocalDateTime now);

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByBookerIdAndEndAfterAndStatusOrderByStartDesc(Long bookerId, LocalDateTime now,
                                                                     BookingStatus bookingStatus);

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByItemOwnerOrderByStartDesc(Long itemId);

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByItemOwnerAndStartBeforeAndEndAfterAndStatusOrderByStartDesc(Long ownerId,
                                                                                    LocalDateTime now1,
                                                                                    LocalDateTime now2,
                                                                                    BookingStatus status);

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByItemOwnerAndEndBeforeAndStatusOrderByStartDesc(Long ownerId, LocalDateTime now,
                                                                       BookingStatus bookingStatus);

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByItemOwnerAndStartAfterAndStatusOrderByStartDesc(Long ownerId, LocalDateTime now,
                                                                        BookingStatus bookingStatus);

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByItemOwnerAndStatusAndEndAfterOrderByStartDesc(Long ownerId,
                                                                      BookingStatus status, LocalDateTime now);

    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> findByItemOwnerAndEndAfterAndStatusOrderByStartDesc(Long ownerId, LocalDateTime now,
                                                                      BookingStatus bookingStatus);

    default Optional<Booking> findLastApproved(Long itemId, LocalDateTime now) {
        return findFirstByItemIdAndEndBeforeAndStatusOrderByEndDesc(itemId, now, BookingStatus.APPROVED);
    }

    Optional<Booking> findFirstByItemIdAndEndBeforeAndStatusOrderByEndDesc(Long itemId, LocalDateTime now,
                                                                           BookingStatus bookingStatus);

    default Optional<Booking> findNextApproved(Long itemId, LocalDateTime now) {
        return findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(itemId, now, BookingStatus.APPROVED);
    }

    Optional<Booking> findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(Long itemId, LocalDateTime now,
                                                                             BookingStatus bookingStatus);

    default boolean hasFinishedBooking(Long itemId, Long bookerId, LocalDateTime now) {
        return existsByItemIdAndBookerIdAndStatusAndEndBefore(itemId, bookerId, BookingStatus.APPROVED, now);
    }

    boolean existsByItemIdAndBookerIdAndStatusAndEndBefore(Long itemId, Long bookerId, BookingStatus bookingStatus,
                                                          LocalDateTime now);
}
