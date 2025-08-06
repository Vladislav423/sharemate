package com.practice.sharemate.booking;


import java.util.List;

public interface BookingService {

        BookingDto findById(long userId, long bookingId);

        BookingDto create(long userId,BookingRequestDto bookingRequestDto);

        BookingDto approve(long userId,long bookingId, boolean approved);

        List<BookingDto> findUserBookings(long userId, String state);

        List<BookingDto> findOwnerBookings(long ownerId, String state);

}
