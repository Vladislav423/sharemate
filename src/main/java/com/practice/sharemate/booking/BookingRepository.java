package com.practice.sharemate.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDateDesc(long bookerId); //ALL

    List<Booking> findByBookerIdAndEndDateBeforeOrderByStartDateDesc(long bookerId, LocalDateTime now); //PAST

    List<Booking> findByBookerIdAndStartDateAfterOrderByStartDate(long bookerId, LocalDateTime now); //FUTURE

    List<Booking> findByBookerIdAndStartDateBeforeAndEndDateIsAfterOrderByStartDateDesc(long bookerId, LocalDateTime now, LocalDateTime end); //CURRENT

    List<Booking> findByBookerIdAndStatusOrderByStartDateDesc(long bookerId, Status status); // WAITING / REJECTED

    List<Booking> findByItemOwnerIdOrderByStartDateDesc(long ownerId);

    List<Booking> findByItemOwnerIdAndEndDateBeforeOrderByStartDateDesc(long ownerId, LocalDateTime now);

    List<Booking> findByItemOwnerIdAndStartDateAfterOrderByStartDate(long ownerId, LocalDateTime now);

    List<Booking> findByItemOwnerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(long ownerId, LocalDateTime now, LocalDateTime end);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDateDesc(long ownerId, Status status);

    List<Booking> findByBookerIdAndItemIdAndStatusAndEndDateIsBefore(long userId,long itemId, Status status, LocalDateTime now);

}
