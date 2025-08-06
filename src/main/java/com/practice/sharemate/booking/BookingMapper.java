package com.practice.sharemate.booking;

import com.practice.sharemate.item.CommentDto;
import com.practice.sharemate.item.CommentMapper;
import com.practice.sharemate.item.ItemDto;
import com.practice.sharemate.user.UserDto;
import org.springframework.stereotype.Component;


import java.util.Collections;
import java.util.List;

@Component
public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setStartDate(booking.getStartDate());
        bookingDto.setEndDate(booking.getEndDate());
        bookingDto.setStatus(booking.getStatus());

        List<CommentDto> commentsDto;

        if (booking.getItem().getComments() != null) {
            commentsDto = booking.getItem().getComments().stream()
                    .map(CommentMapper::toCommentDto)
                    .toList();
        } else {
            commentsDto = Collections.emptyList();
        }


        ItemDto itemDto = new ItemDto(booking.getItem().getId(), booking.getItem().getName(), booking.getItem().getDescription(), booking.getItem().getAvailable(), commentsDto);
        bookingDto.setItem(itemDto);

        UserDto bookDto = new UserDto(booking.getBooker().getId(), booking.getBooker().getName(), booking.getBooker().getEmail());
        bookingDto.setBooker(bookDto);

        return bookingDto;

    }
}
